package sgtmelon.scriptum.infrastructure.utils

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.infrastructure.model.key.PermissionRequest
import sgtmelon.scriptum.infrastructure.model.state.PermissionState

inline fun <reified F : Fragment> FragmentManager.getFragmentByTag(tag: String): F? {
    return findFragmentByTag(tag) as? F
}

@Deprecated("Need do something with original function")
fun Fragment.requestPermission(request: PermissionRequest, state: PermissionState) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(arrayOf(state.permission), request.ordinal)
    }
}