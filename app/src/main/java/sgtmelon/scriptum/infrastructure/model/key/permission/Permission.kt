package sgtmelon.scriptum.infrastructure.model.key.permission

import android.Manifest
import sgtmelon.scriptum.data.model.PermissionKey

/**
 * Class for hold information about permission key (taken from [Manifest]).
 */
sealed class Permission(value: String) {

    val key = PermissionKey(value)

    object WriteExternalStorage : Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

}