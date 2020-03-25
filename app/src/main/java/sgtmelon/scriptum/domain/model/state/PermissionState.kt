package sgtmelon.scriptum.domain.model.state

import android.app.Activity
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.extension.isGranted

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