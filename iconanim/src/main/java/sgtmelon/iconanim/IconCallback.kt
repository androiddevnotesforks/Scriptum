package sgtmelon.iconanim

import android.graphics.drawable.AnimatedVectorDrawable

/**
 * Interface for [IconAnimControl] and for UI classes
 *
 * @author SerjantArbuz
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