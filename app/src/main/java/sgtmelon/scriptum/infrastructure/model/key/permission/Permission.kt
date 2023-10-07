package sgtmelon.scriptum.infrastructure.model.key.permission

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider.Version
import sgtmelon.scriptum.data.model.PermissionKey

/**
 * Class for hold information about permission key (taken from [Manifest]).
 *
 * [applyVersion] - [Build.VERSION_CODES] when this permission was added. If current version
 * lower than [applyVersion] -> request permission isn't needed.
 *
 * [expireVersion] - [Build.VERSION_CODES] when this permission stop working. If current version
 * bigger or equals than [expireVersion] -> need handle permission in other way.
 */
@SuppressLint("InlinedApi") // Suspend api check
sealed class Permission(val value: String, val applyVersion: Int?, val expireVersion: Int?) {

    val key = PermissionKey(value)

    val isOldApi get() = applyVersion != null && Version.current < applyVersion
    val isNewApi get() = expireVersion != null && Version.current >= expireVersion

    /** Starting from TIRAMISU version we use ScopedStorage, and don't need ask for permission. */
    object WriteExternalStorage : Permission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        applyVersion = null,
        expireVersion = Build.VERSION_CODES.TIRAMISU
    )

    /** This permission required only starting from TIRAMISU, don't ask for older versions. */
    object Notifications : Permission(
        Manifest.permission.POST_NOTIFICATIONS,
        applyVersion = Build.VERSION_CODES.TIRAMISU,
        expireVersion = null
    )
}