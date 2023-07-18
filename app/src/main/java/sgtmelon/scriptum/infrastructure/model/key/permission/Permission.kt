package sgtmelon.scriptum.infrastructure.model.key.permission

import android.Manifest

/**
 * Class for hold information about permission key (taken from [Manifest]).
 */
sealed class Permission(val value: String) {

    object WriteExternalStorage : Permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

}