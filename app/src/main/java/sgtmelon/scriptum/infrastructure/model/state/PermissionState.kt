package sgtmelon.scriptum.infrastructure.model.state

import android.app.Activity
import sgtmelon.scriptum.data.model.PermissionKey
import sgtmelon.scriptum.infrastructure.model.key.Permission
import sgtmelon.scriptum.infrastructure.model.key.PermissionResult
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel
import sgtmelon.scriptum.infrastructure.utils.extensions.isNotGranted
import timber.log.Timber

/**
 * State for permission request.
 */
class PermissionState private constructor(val key: PermissionKey) {

    constructor(permission: Permission) : this(PermissionKey(permission.value))

    fun getResult(activity: Activity?, viewModel: PermissionViewModel): PermissionResult? {
        if (activity == null) return null

        val isCalled = viewModel.isCalled(key)
        val checkPermission = activity.checkSelfPermission(key.value)
        val showExplanation = activity.shouldShowRequestPermissionRationale(key.value)

        Timber.i(
            message = "Permission: ${key.value} | isCalled=$isCalled, " +
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
        return if (checkPermission.isNotGranted()) {
            if (showExplanation || !isCalled) {
                PermissionResult.ASK
            } else {
                PermissionResult.FORBIDDEN
            }
        } else {
            PermissionResult.GRANTED
        }
    }
}