package sgtmelon.scriptum.presentation.control.file

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import sgtmelon.extension.getTime
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem
import java.io.*

/**
 * Class for help control manipulations with files.
 */
class FileControl(private val context: Context) : IFileControl {

    override val appDirectory: File = context.filesDir

    override val cacheDirectory: File = context.cacheDir


    override fun readFile(path: String): String? {
        try {
            val inputStream = FileInputStream(File(path))
            val result = readInputStream(inputStream)
            inputStream.close()

            return result
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
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
        val file = File(Environment.getExternalStorageDirectory(), name)

        try {
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            writeOutputStream(outputStream, data)
            outputStream.flush()
            outputStream.close()

            return file.path
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

        return null
    }

    private fun writeOutputStream(outputStream: OutputStream, data: String) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        bufferedWriter.append(data)
        bufferedWriter.close()
    }

    override fun getTimeName(@FileType type: String): String = getTime().plus(type)


    override suspend fun getFileList(@FileType type: String): List<FileItem> {
        val list = mutableListOf<FileItem>()

        ContextCompat.getExternalFilesDirs(context, null).filterNotNull().forEach {
            list.addAll(getFileList(it, type))
        }

        ContextCompat.getExternalCacheDirs(context).filterNotNull().forEach {
            list.addAll(getFileList(it, type))
        }

        return list
    }

    private suspend fun getFileList(directory: File, @FileType type: String): List<FileItem> {
        val list = mutableListOf<FileItem>()

        directory.listFiles()?.forEach {
            when {
                it.isDirectory -> list.addAll(getFileList(it, type))
                it.endsWith(type) -> list.add(FileItem(it.nameWithoutExtension, it.path))
            }
        }

        return list
    }

    companion object {
        private val TAG = FileControl::class.java.simpleName
    }

}