package sgtmelon.scriptum.infrastructure.utils.extensions.insets

import android.graphics.Rect
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

inline fun View.doOnApplyWindowInsets(
    crossinline block: (
        view: View,
        insets: WindowInsetsCompat,
        isFirstTime: Boolean,
        padding: Rect,
        margin: Rect
    ) -> WindowInsetsCompat
) {
    val initPadding = getInitPadding()
    val initMargin = getInitMargin()

    /** Variable for detect first applying of insets. */
    var isFirstTime = true

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val result = block(view, insets, isFirstTime, initPadding, initMargin)

        if (isFirstTime) {
            isFirstTime = false
        }

        return@setOnApplyWindowInsetsListener result
    }

    requestApplyInsetsWhenAttached()
}

fun View.removeWindowInsetsListener() {
    ViewCompat.setOnApplyWindowInsetsListener(this, null)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View) {
                v.addOnAttachStateChangeListener(this)
            }

            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }
        })
    }
}

fun WindowInsetsCompat.removeSystemWindowInsets(dir: InsetsDir): WindowInsetsCompat {
    val newRect = Rect(
        if (dir == InsetsDir.LEFT) 0 else systemWindowInsetLeft,
        if (dir == InsetsDir.TOP) 0 else systemWindowInsetTop,
        if (dir == InsetsDir.RIGHT) 0 else systemWindowInsetRight,
        if (dir == InsetsDir.BOTTOM) 0 else systemWindowInsetBottom
    )

    return WindowInsetsCompat.Builder(this)
        .setSystemWindowInsets(Insets.of(newRect))
        .build()
}