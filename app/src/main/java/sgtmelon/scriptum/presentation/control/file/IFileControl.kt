package sgtmelon.scriptum.presentation.control.file

import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem

/**
 * Interface for [FileControl].
 */
interface IFileControl {

    val appDirectory: String

    val cacheDirectory: String


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getTimeName(@FileType type: String): String


    suspend fun getFileList(@FileType type: String): List<FileItem>

}