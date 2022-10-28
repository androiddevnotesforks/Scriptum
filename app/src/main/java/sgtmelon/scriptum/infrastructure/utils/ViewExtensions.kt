package sgtmelon.scriptum.infrastructure.utils

import android.view.View

inline fun View.makeVisible(condition: Boolean, onOtherwise: () -> Unit = { makeGone() }) {
    if (condition) makeVisible() else onOtherwise()
}

fun View.makeVisible() = apply { visibility = View.VISIBLE }

fun View.makeInvisible() = apply { visibility = View.INVISIBLE }

fun View.makeGone() = apply { visibility = View.GONE }

fun View?.isVisible(): Boolean = this?.visibility == View.VISIBLE

fun View?.isInvisible(): Boolean = this?.visibility == View.INVISIBLE

fun View?.isGone(): Boolean = this?.visibility == View.GONE