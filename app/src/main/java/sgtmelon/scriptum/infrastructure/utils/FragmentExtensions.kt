package sgtmelon.scriptum.infrastructure.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.infrastructure.model.key.PermissionRequest

inline fun <reified F : Fragment> FragmentManager.getFragmentByTag(tag: String): F? {
    return findFragmentByTag(tag) as? F
}

@Deprecated("Need do something with original function")
fun Fragment.requestPermissions(request: PermissionRequest, vararg permissions: String) {
    requestPermissions(permissions, request.ordinal)
}