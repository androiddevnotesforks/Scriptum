package sgtmelon.safedialog.utils

import android.app.Dialog
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import sgtmelon.safedialog.R

fun Dialog.applyAnimation() = apply {
    window?.attributes?.windowAnimations = R.style.SafeDialog_Animation
}

fun Dialog.applyTransparentBackground() = apply {
    window?.setBackgroundDrawableResource(android.R.color.transparent)
}

fun DialogFragment.safeShow(tag: String?, owner: DialogOwner) {
    owner.lifecycleScope.launchWhenResumed {
        if (!isAdded) {
            show(owner.fm, tag)
        }
    }
}

fun DialogFragment.safeDismiss(owner: LifecycleOwner) {
    owner.lifecycleScope.launchWhenResumed {
        /**
         * If check isAdded key - may be a case when dialog show func done buy not it not added yet,
         * and this check (if (isAdded) dismiss()) will not work properly.
         */
        dismiss()
    }
}

fun Dialog.showKeyboard() = apply {
    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}