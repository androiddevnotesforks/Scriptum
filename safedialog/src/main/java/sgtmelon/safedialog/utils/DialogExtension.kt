package sgtmelon.safedialog

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun Dialog.applyAnimation() = apply {
    window?.attributes?.windowAnimations = R.style.SafeDialog_Animation
}

fun DialogFragment.safeShow(fm: FragmentManager, tag: String?) {
    if (isAdded) return

    show(fm, tag)
}