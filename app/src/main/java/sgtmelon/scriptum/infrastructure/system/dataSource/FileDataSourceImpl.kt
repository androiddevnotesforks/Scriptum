package sgtmelon.scriptum.infrastructure.system.dataSource

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import sgtmelon.common.utils.getCalendarText
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.model.exception.FilesException
import sgtmelon.scriptum.infrastructure.model.item.FileItem
import sgtmelon.scriptum.infrastructure.model.type.FileType
import sgtmelon.scriptum.infrastructure.utils.record

/**
 * Class for help control manipulations with files.
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
     * Return path to created file.
     */
    override fun writeFile(name: String, data: String): String? {
        val file = File(saveDirectory, name)

        try {
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            writeOutputStream(outputStream, data)
            outputStream.flush()
            outputStream.close()

            return file.path
        } catch (e: Throwable) {
            FilesException(e).record()
        }

        return null
    }

    private fun writeOutputStream(outputStream: OutputStream, data: String) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        bufferedWriter.append(data)
        bufferedWriter.close()
    }

    override fun getTimeName(@FileType type: String): String = getCalendarText().plus(type)


    override suspend fun getBackupFileList(): List<FileItem> = getFileList(FileType.BACKUP)

    private suspend fun getFileList(@FileType type: String): List<FileItem> {
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

    private suspend fun getFileList(directory: File, @FileType type: String): List<FileItem> {
        val fileList = directory.listFiles() ?: return emptyList()

        return ArrayList<FileItem>().apply {
            for (it in fileList) {
                when {
                    it.isDirectory -> addAll(getFileList(it, type))
                    it.name.endsWith(type) -> add(FileItem(it.nameWithoutExtension, it.path))
                }
            }
        }
    }
}