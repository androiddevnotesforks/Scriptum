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
import sgtmelon.scriptum.infrastructure.converter.UriConverter
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
class FileDataSourceImpl(
    private val context: Context,
    private val uriConverter: UriConverter
) : FileDataSource {

    //region Read/Write file

    override fun readFileFromPath(value: String): String? = readFile {
        FileInputStream(File(value))
    }

    override fun readFileFromUri(value: String): String? = readFile {
        val uri = uriConverter.toUri(value) ?: return@readFile null
        context.contentResolver.openInputStream(uri)
    }

    private inline fun readFile(getInputStream: () -> InputStream?): String? {
        try {
            val stream = getInputStream() ?: throw FilesException.OpenInputStream
            val result = readInputStream(stream)
            stream.close()

            return result
        } catch (e: Throwable) {
            e.record()
        }

        return null
    }

    private fun readInputStream(stream: InputStream) = StringBuilder().apply {
        val reader = BufferedReader(InputStreamReader(stream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            append(line).append("\n")
        }

        reader.close()
    }.toString()

    /** Returned value is a path to created file. [name] must contain extension. */
    override fun writeFile(name: String, data: String): String? {
        var stream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, savePath)
                }

                val resolver = context.contentResolver
                val pathUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val fileUri = resolver.insert(pathUri, values) ?: throw FilesException.ContentInsert
                stream = resolver.openOutputStream(fileUri) ?: throw FilesException.OpenOutputStream
                writeOutputStream(stream, data)

                return savePath.plus(name)
            } else {
                val file = File(savePath, name)
                file.createNewFile()

                stream = FileOutputStream(file)
                writeOutputStream(stream, data)

                return file.path
            }
        } catch (e: Throwable) {
            e.record()
        } finally {
            stream?.flush()
            stream?.close()
        }

        return null
    }

    private fun writeOutputStream(outputStream: OutputStream, data: String) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        bufferedWriter.append(data)
        bufferedWriter.close()
    }

    //endregion

    //region Work with names, files and directories

    override fun getTimeName(type: FileType): String = getCalendarText().plus(type.extension)

    override val savePath: String = Environment.DIRECTORY_DOCUMENTS
        .plus(File.separator)
        .plus(context.getString(R.string.app_name))
        .plus(File.separator)

    override val externalDirectory: File
        get() = Environment.getExternalStorageDirectory()

    override val externalFiles: List<File>
        get() = ContextCompat.getExternalFilesDirs(context, null).filterNotNull()

    override val externalCache: List<File>
        get() = ContextCompat.getExternalCacheDirs(context).filterNotNull()

    //endregion

    override suspend fun getBackupFileList(): List<FileItem> = getFileList(FileType.BACKUP)

    private suspend fun getFileList(type: FileType): List<FileItem> {
        val list = mutableListOf<FileItem>()

        list.addAll(getFileList(externalDirectory, type))
        externalFiles.forEach { list.addAll(getFileList(it, type)) }
        externalCache.forEach { list.addAll(getFileList(it, type)) }

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