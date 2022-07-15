package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef

@IntDef(PermissionRequest.MELODY, PermissionRequest.EXPORT, PermissionRequest.IMPORT)
annotation class PermissionRequest {
    companion object {
        const val MELODY = 0
        const val EXPORT = 1
        const val IMPORT = 2
    }
}