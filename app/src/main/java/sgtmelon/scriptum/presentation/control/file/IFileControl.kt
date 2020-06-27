package sgtmelon.scriptum.presentation.control.file

import sgtmelon.scriptum.domain.model.annotation.Type

/**
 * Interface for [FileControl].
 */
interface IFileControl {

    val appDirectory: String

    val cacheDirectory: String


    fun readFile(path: String): String?

    fun writeFile(name: String, data: String)

    fun getTimeName(@Type type: String): String


    fun getPathList(@Type type: String): List<String>

}