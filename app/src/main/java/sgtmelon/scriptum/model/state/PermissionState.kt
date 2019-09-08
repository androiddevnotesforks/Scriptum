package sgtmelon.scriptum.model.state

import android.app.Activity
import android.os.Build
import sgtmelon.scriptum.extension.isGranted
import sgtmelon.scriptum.model.key.PermissionResult

/**
 * State for permission request
 *
 * @author SerjantArbuz
 */
class PermissionState(private val activity: Activity, val permission: String) {

    fun getResult(): PermissionResult {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return PermissionResult.LOW_API

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