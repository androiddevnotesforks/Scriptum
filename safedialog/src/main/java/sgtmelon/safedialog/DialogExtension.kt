package sgtmelon.safedialog

import android.app.Dialog

fun Dialog.applyAnimation() = apply {
    window?.attributes?.windowAnimations = R.style.SafeDialog_Animation
}