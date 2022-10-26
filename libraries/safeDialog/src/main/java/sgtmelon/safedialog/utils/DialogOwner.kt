package sgtmelon.safedialog.utils

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

interface DialogOwner : LifecycleOwner {

    val fm: FragmentManager
}