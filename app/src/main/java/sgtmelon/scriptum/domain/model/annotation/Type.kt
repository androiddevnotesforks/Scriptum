package sgtmelon.scriptum.domain.model.annotation

import androidx.annotation.StringDef

/**
 * Describes files extension (type).
 */
@StringDef(Type.BACKUP)
annotation class Type {
    companion object {
        const val BACKUP = ".backup"
    }
}