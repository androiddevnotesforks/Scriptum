package sgtmelon.scriptum.infrastructure.utils.extensions

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.infrastructure.listener.OnTouchSelectionListener

inline fun View.makeVisibleIf(condition: Boolean, otherwise: View.() -> Unit = { makeGone() }) {
    if (condition) makeVisible() else otherwise()
}

fun View.makeVisible() = apply { visibility = View.VISIBLE }

fun View.makeInvisible() = apply { visibility = View.INVISIBLE }

fun View.makeGone() = apply { visibility = View.GONE }

fun View?.isVisible(): Boolean = this?.visibility == View.VISIBLE

fun View?.isInvisible(): Boolean = this?.visibility == View.INVISIBLE

fun View?.isGone(): Boolean = this?.visibility == View.GONE

fun View.resetAlpha() = apply { alpha = ALPHA_MAX }

/** Return true if [MotionEvent] happened inside [view] rectangle. */
fun MotionEvent?.onView(view: View?): Boolean {
    if (view == null || this == null) return false

    return Rect().apply {
        view.getGlobalVisibleRect(this)
    }.contains(rawX.toInt(), rawY.toInt())
}

fun Toolbar.getItem(@IdRes id: Int): MenuItem = menu.findItem(id)

@SuppressLint("ClickableViewAccessibility")
fun View.setOnTouchSelectionListener(editText: EditText?) {
    setOnTouchListener(OnTouchSelectionListener(editText))
}