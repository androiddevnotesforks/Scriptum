package sgtmelon.iconanim

import android.graphics.drawable.AnimatedVectorDrawable

/**
 * Callback for change drawables and run animation.
 */
interface IconChangeCallback {

    /**
     * [needAnim] - need for start [AnimatedVectorDrawable].
     */
    fun setDrawable(isEnterIcon: Boolean, needAnim: Boolean)
}