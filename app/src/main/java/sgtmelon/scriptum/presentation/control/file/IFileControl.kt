package sgtmelon.scriptum.presentation.control.file

import sgtmelon.scriptum.domain.model.annotation.Type
import sgtmelon.scriptum.domain.model.item.FileItem

/**
 * Interface for [FileControl].
 */
interface IFileControl {

    val appDirectory: String

    val cacheDirectory: String


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String): String?

    fun getTimeName(@Type type: String): String


    fun getFileList(@Type type: String): List<FileItem>

}