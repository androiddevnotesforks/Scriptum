package sgtmelon.scriptum.domain.model.annotation

import androidx.annotation.IntDef

@IntDef(PermissionRequest.MELODY, PermissionRequest.IMPORT)
annotation class PermissionRequest {
    companion object {
        const val MELODY = 0
        const val IMPORT = 1
    }
}