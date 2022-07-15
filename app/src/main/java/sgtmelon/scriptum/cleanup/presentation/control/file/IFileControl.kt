package sgtmelon.scriptum.cleanup.presentation.control.file

import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
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