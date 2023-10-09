package sgtmelon.scriptum.infrastructure.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.model.key.permission.PermissionResult
import sgtmelon.scriptum.infrastructure.model.state.PermissionState
import sgtmelon.scriptum.infrastructure.screen.parent.permission.PermissionViewModel

/** For short check permission is Granted or Denied. */
fun Int.isGranted() = this == PackageManager.PERMISSION_GRANTED
fun Int.isNotGranted() = !isGranted()

fun Boolean.toPermissionResult(): PermissionResult {
    return if (this) PermissionResult.GRANTED else PermissionResult.FORBIDDEN
}

fun Context.isPermissionGranted(permission: Permission): Boolean {
    return checkSelfPermission(permission.value).isGranted()
}

fun ActivityResultLauncher<String>.launch(state: PermissionState, viewModel: PermissionViewModel) {
    viewModel.setCalled(state.permission.key)
    launch(state.permission.value)
}

/** Important to register request before [Fragment.onStart] lifecycle call. */
fun Fragment.registerPermissionRequest(
    onResult: (isGranted: Boolean) -> Unit
): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { onResult(it) }
}

fun Fragment.registerFileRequest(
    onResult: (uri: Uri?) -> Unit
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        onResult(it?.data?.data)
    }
}