package sgtmelon.scriptum.infrastructure.model.state

import android.app.Activity
import sgtmelon.scriptum.cleanup.extension.notGranted
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider.Version
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult

/**
 * State for permission request.
 */
class PermissionState(val permission: String) {

    fun getResult(activity: Activity?): PermissionResult? {
        if (Version.isMarshmallowLess()) return PermissionResult.LOW_API

        if (activity == null) return null

        return if (activity.checkSelfPermission(permission).notGranted()) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                PermissionResult.ASK
            } else {
                PermissionResult.FORBIDDEN
            }
        } else {
            PermissionResult.GRANTED
        }
    }
}