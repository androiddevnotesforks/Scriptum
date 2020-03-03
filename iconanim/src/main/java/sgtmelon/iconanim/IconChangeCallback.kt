package sgtmelon.iconanim

import android.graphics.drawable.AnimatedVectorDrawable

/**
 * Interface for communicate with [IconAnimControl]
 */
interface IconChangeCallback {

    /**
     * [needAnim] - need only for [AnimatedVectorDrawable]
     */
    fun setDrawable(enterIcon: Boolean, needAnim: Boolean)

}