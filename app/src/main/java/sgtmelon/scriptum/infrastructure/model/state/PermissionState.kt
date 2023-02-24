package sgtmelon.scriptum.infrastructure.model.state

import android.app.Activity
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.infrastructure.utils.extensions.notGranted

/**
 * State for permission request.
 */
class PermissionState(val permission: String) {

    fun getResult(activity: Activity?): PermissionResult? {
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