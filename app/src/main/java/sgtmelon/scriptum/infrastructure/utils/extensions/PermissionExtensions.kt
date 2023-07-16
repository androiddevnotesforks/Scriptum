package sgtmelon.scriptum.infrastructure.utils.extensions

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.infrastructure.model.key.PermissionRequest
import sgtmelon.scriptum.infrastructure.model.state.PermissionState
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel

/** For short check permission is Granted or Denied. */
fun Int.isGranted() = this == PackageManager.PERMISSION_GRANTED
fun Int.isNotGranted() = !isGranted()

@Deprecated("Need do something with original function")
fun Fragment.requestPermission(
    request: PermissionRequest,
    state: PermissionState,
    viewModel: PermissionViewModel
) {
    viewModel.setCalled(state.key)
    requestPermissions(arrayOf(state.key.value), request.ordinal)
}