package sgtmelon.scriptum.cleanup.domain.model.state

import android.app.Activity
import sgtmelon.scriptum.cleanup.domain.model.key.PermissionResult
import sgtmelon.scriptum.cleanup.extension.notGranted
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider.Version

/**
 * State for permission request
 */
class PermissionState(val permission: String) {

    fun getResult(activity: Activity?): PermissionResult? {
        if (Version.isMarshmallowLess()) return PermissionResult.LOW_API

        if (activity == null) return null

        return if (activity.checkSelfPermission(permission).notGranted()) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                PermissionResult.ALLOWED
            } else {
                PermissionResult.FORBIDDEN
            }
        } else {
            PermissionResult.GRANTED
        }
    }
}