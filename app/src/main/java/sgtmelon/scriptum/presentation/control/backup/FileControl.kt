package sgtmelon.scriptum.presentation.control.backup

import android.util.Log
import sgtmelon.extension.getTime
import sgtmelon.scriptum.domain.model.annotation.Type
import sgtmelon.scriptum.presentation.control.backup.callback.IFileControl
import java.io.*

/**
 * Class for help control manipulations with files.
 */
class FileControl : IFileControl {

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

    override fun writeFile(directory: String, name: String, data: String) {
        val parent = File(directory)
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
        return getTime().plus(" scriptum").plus(type)
    }

    override fun getPathList(@Type type: String): List<String> {
        TODO("Not yet implemented")
    }


    companion object {
        private val TAG = FileControl::class.java.simpleName
    }

}