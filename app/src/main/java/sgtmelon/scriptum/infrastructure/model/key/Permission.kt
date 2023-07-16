package sgtmelon.scriptum.infrastructure.model.key

import android.Manifest

sealed class Permission(val value: String) {

    object WriteExternalStorage : Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

}