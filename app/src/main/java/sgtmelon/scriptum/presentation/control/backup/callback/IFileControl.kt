package sgtmelon.scriptum.presentation.control.backup.callback

import sgtmelon.scriptum.domain.model.annotation.Type
import sgtmelon.scriptum.presentation.control.backup.FileControl

/**
 * Interface for [FileControl].
 */
interface IFileControl {

    fun readFile(path: String): String?

    fun writeFile(directory: String, name: String, data: String)

    fun getTimeName(@Type type: String): String

    fun getPathList(@Type type: String): List<String>

}