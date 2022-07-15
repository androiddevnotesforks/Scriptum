package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.StringDef

/**
 * Describes files extension (type).
 */
@StringDef(FileType.BACKUP)
annotation class FileType {
    companion object {
        const val BACKUP = ".backup"
    }
}