package sgtmelon.safedialog.utils

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import sgtmelon.safedialog.R

fun Dialog.applyAnimation() = apply {
    window?.attributes?.windowAnimations = R.style.SafeDialog_Animation
}

fun DialogFragment.safeShow(fm: FragmentManager, tag: String?, owner: LifecycleOwner) {
    owner.lifecycleScope.launchWhenResumed {
        if (!isAdded) {
            show(fm, tag)
        }
    }
}

fun DialogFragment.safeDismiss(owner: LifecycleOwner) {
    owner.lifecycleScope.launchWhenResumed {
        if (isAdded) {
            dismiss()
        }
    }
}