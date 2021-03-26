package sgtmelon.scriptum.domain.model.state

import android.app.Activity
import sgtmelon.scriptum.domain.model.key.PermissionResult
import sgtmelon.scriptum.extension.notGranted
import sgtmelon.scriptum.presentation.provider.BuildProvider.Version

/**
 * State for permission request
 */
class PermissionState(val permission: String, private val activity: Activity?) {

    fun getResult(): PermissionResult? {
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