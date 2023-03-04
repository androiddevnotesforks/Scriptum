package sgtmelon.safedialog.utils

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

/** Used for work with [safeShow] functions. */
interface DialogOwner : LifecycleOwner {

    val fm: FragmentManager
}