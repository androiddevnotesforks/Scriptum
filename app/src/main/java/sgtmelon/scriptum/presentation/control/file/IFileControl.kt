package sgtmelon.scriptum.presentation.control.file

import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem
import java.io.File

/**
 * Interface for [FileControl].
 */
interface IFileControl {

    val saveDirectory: File

    fun getExternalFiles(): List<File>

    fun getExternalCache(): List<File>


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getTimeName(@FileType type: String): String


    suspend fun getFileList(@FileType type: String): List<FileItem>

}