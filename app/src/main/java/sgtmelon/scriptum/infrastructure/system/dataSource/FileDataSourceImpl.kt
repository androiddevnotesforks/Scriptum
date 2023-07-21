package sgtmelon.scriptum.infrastructure.system.dataSource

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import sgtmelon.extensions.getCalendarText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.model.exception.FilesException
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.key.FileType
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

/**
 * Class for working and any manipulations with files.
 */
class FileDataSourceImpl(private val context: Context) : FileDataSource {

    override val saveDirectory: File get() = Environment.getExternalStorageDirectory()

    override fun getExternalFiles(): List<File> {
        return ContextCompat.getExternalFilesDirs(context, null).filterNotNull()
    }

    override fun getExternalCache(): List<File> {
        return ContextCompat.getExternalCacheDirs(context).filterNotNull()
    }

    override fun readFile(path: String): String? {
        try {
            val inputStream = FileInputStream(File(path))
            val result = readInputStream(inputStream)
            inputStream.close()

            return result
        } catch (e: Throwable) {
            FilesException(e).record()
        }

        return null
    }

    private fun readInputStream(inputStream: InputStream) = StringBuilder().apply {
        val reader = BufferedReader(InputStreamReader(inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            append(line).append("\n")
        }

        reader.close()
    }.toString()

    /**
     * Returned value is a path to created file. [name] must contain extension.
     */
    override fun writeFile(name: String, data: String): String? {
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val path = Environment.DIRECTORY_DOCUMENTS
                    .plus(File.separator)
                    .plus(context.getString(R.string.app_name))

                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, path)
                }

                val resolver = context.contentResolver
                val pathUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val fileUri = resolver.insert(pathUri, values) ?: throw NullPointerException()
                outputStream = resolver.openOutputStream(fileUri) ?: throw NullPointerException()
                writeOutputStream(outputStream, data)

                return path.plus(File.separator).plus(name)
            } else {
                val file = File(saveDirectory, name)
                file.createNewFile()

                outputStream = FileOutputStream(file)
                writeOutputStream(outputStream, data)

                return file.path
            }
        } catch (e: Throwable) {
            FilesException(e).record()
        } finally {
            outputStream?.flush()
            outputStream?.close()
        }

        return null
    }

    private fun writeOutputStream(outputStream: OutputStream, data: String) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        bufferedWriter.append(data)
        bufferedWriter.close()
    }

    override fun getTimeName(type: FileType): String = getCalendarText().plus(type.extension)

    override suspend fun getBackupFileList(): List<FileItem> = getFileList(FileType.BACKUP)

    private suspend fun getFileList(type: FileType): List<FileItem> {
        val list = mutableListOf<FileItem>()

        list.addAll(getFileList(saveDirectory, type))

        for (it in getExternalFiles()) {
            list.addAll(getFileList(it, type))
        }

        for (it in getExternalCache()) {
            list.addAll(getFileList(it, type))
        }

        return list.sortedByDescending { it.name }
    }

    @Suppress("RedundantSuspendModifier")
    private suspend fun getFileList(directory: File, type: FileType): List<FileItem> {
        val fileList = directory.listFiles() ?: return emptyList()

        return ArrayList<FileItem>().apply {
            for (it in fileList) {
                when {
                    it.isDirectory -> addAll(getFileList(it, type))
                    it.name.endsWith(type.extension) -> {
                        add(FileItem(it.nameWithoutExtension, it.path, type))
                    }
                }
            }
        }
    }
}