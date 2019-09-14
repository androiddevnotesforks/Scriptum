package sgtmelon.iconanim

import android.graphics.drawable.AnimatedVectorDrawable

/**
 * Interface for comminication with [IconAnimControl] and for UI classes
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