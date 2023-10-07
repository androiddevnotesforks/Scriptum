package sgtmelon.scriptum.infrastructure.model.state

import android.app.Activity
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.utils.extensions.isGranted
import timber.log.Timber

/**
 * State for permission request.
 */
class PermissionState(val permission: Permission) {

    fun getResult(activity: Activity?, viewModel: PermissionViewModel): PermissionResult? {
        if (activity == null) return null

        if (permission.isOldApi) return PermissionResult.OLD_API
        if (permission.isNewApi) return PermissionResult.NEW_API

        val checkPermission = activity.checkSelfPermission(permission.value)
        val showExplanation = activity.shouldShowRequestPermissionRationale(permission.value)
        val isCalled = viewModel.isCalled(permission.key)

        Timber.i(
            message = "Permission: ${permission.value} | isCalled=$isCalled, " +
                    "checkPermission=$checkPermission, showExplanation=$showExplanation"
        )

        /**
         * Cases:
         * - First install: flag=-1, shouldShow=false
         * - Deny before: flag=-1, shouldShow=true
         * - Allow before: flag=0, shouldShow=false
         * - Don't ask before: flag=-1, shouldShow=false
         * - After switch in settings: flag=-1, shouldShow=true
         */
        return if (checkPermission.isGranted()) {
            PermissionResult.GRANTED
        } else {
            if (showExplanation || !isCalled) {
                PermissionResult.ASK
            } else {
                PermissionResult.FORBIDDEN
            }
        }
    }
}