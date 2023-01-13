package sgtmelon.iconanim.callback

import android.graphics.drawable.AnimatedVectorDrawable

/**
 * Callback for change drawables and run animation.
 */
interface IconChangeCallback {

    /**
     * Null value if it wasn't set ([setDrawable] not called).
     */
    val isEnterIcon: Boolean?

    /**
     * [needAnim] - need for start [AnimatedVectorDrawable].
     */
    fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean)
}