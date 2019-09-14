package sgtmelon.iconanim

import android.graphics.drawable.AnimatedVectorDrawable

/**
 * Interface for communicate with [IconAnimControl]
 */
interface IconCallback {

    /**
     * [needAnim] - need only for [AnimatedVectorDrawable]
     */
    fun setDrawable(drawableOn: Boolean, needAnim: Boolean)

    /**
     * Block button fow prevent lags
     */
    fun setEnabled(enabled: Boolean)

}