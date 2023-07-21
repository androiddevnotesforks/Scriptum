package sgtmelon.scriptum.infrastructure.model.item

import sgtmelon.scriptum.infrastructure.model.key.FileType

/**
 * Model which help describes any files.
 *
 * [name] - name without an extension
 */
data class FileItem(val name: String, val path: String, val type: FileType)