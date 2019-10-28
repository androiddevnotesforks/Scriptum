package sgtmelon.scriptum.model.state

import android.app.Activity
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import sgtmelon.scriptum.extension.isGranted
import sgtmelon.scriptum.model.key.PermissionResult

/**
 * State for permission request
 */
class PermissionState(private val activity: Activity, val permission: String) {

    fun getResult(): PermissionResult {
        if (VERSION.SDK_INT < VERSION_CODES.M) return PermissionResult.LOW_API

        return if (!activity.checkSelfPermission(permission).isGranted()) {
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