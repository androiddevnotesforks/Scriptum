package sgtmelon.scriptum.presentation.control.file

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Type
import java.io.*

/**
 * Class for help control manipulations with files.
 */
class FileControl(private val context: Context) : IFileControl {

    override val appDirectory: String = context.filesDir.path

    override val cacheDirectory: String = context.cacheDir.path


    override fun readFile(path: String): String? {
        try {
            val inputStream = FileInputStream(File(path))
            val result = readInputStream(inputStream)
            inputStream.close()

            return result
        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
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

    override fun writeFile(name: String, data: String) {
        val parent = File(appDirectory)
        val file = File(parent, name)

        /**
         * Create directory if not exist
         */
        if (!parent.exists()) parent.mkdir()

        try {
            file.createNewFile()

            val outputStream = FileOutputStream(file)
            writeOutputStream(outputStream, data)
            outputStream.flush()
            outputStream.close()
        } catch (exception: Exception) {
            Log.e(TAG, exception.toString())
        }
    }

    private fun writeOutputStream(outputStream: OutputStream, data: String) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        bufferedWriter.append(data)
        bufferedWriter.close()
    }

    override fun getTimeName(@Type type: String): String {
        return getTime()
                .plus(other = " ")
                .plus(context.getString(R.string.app_name))
                .plus(type)
    }


    override fun getPathList(@Type type: String): List<String> {
        val list = mutableListOf<String>()

        ContextCompat.getExternalFilesDirs(context, null).filterNotNull().forEach {
            list.addAll(getPathList(it, type))
        }

        ContextCompat.getExternalCacheDirs(context).filterNotNull().forEach {
            list.addAll(getPathList(it, type))
        }

        return list
    }

    private fun getPathList(directory: File, @Type type: String): List<String> {
        val list = mutableListOf<String>()

        directory.listFiles()?.forEach {
            when {
                it.isDirectory -> list.addAll(getPathList(it, type))
                it.endsWith(type) -> list.add(it.path)
            }
        }

        return list
    }


    companion object {
        private val TAG = FileControl::class.java.simpleName
    }

}