package sgtmelon.scriptum.infrastructure.adapter.animation

import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getAlphaAnimator
import sgtmelon.scriptum.cleanup.extension.getElevationAnimator
import sgtmelon.scriptum.cleanup.extension.getScaleXAnimator
import sgtmelon.scriptum.cleanup.extension.getScaleYAnimator

class TouchAnimation {

    private var isChanged = false

    /**
     * Change alpha, scale and elevation on drag start.
     */
    fun onDrag(viewHolder: RecyclerView.ViewHolder?) {
        val cardView = viewHolder?.itemView as? CardView ?: return

        AnimatorSet().apply {
            this.duration = ANIM_DURATION
            this.interpolator = AccelerateDecelerateInterpolator()

            playTogether(
                getAlphaAnimator(cardView, ALPHA_DRAG_MIN),
                getScaleXAnimator(cardView, SCALE_MAX),
                getScaleYAnimator(cardView, SCALE_MAX),
                getElevationAnimator(cardView, R.dimen.elevation_6dp)
            )
        }.start()

        isChanged = true
    }

    /**
     * Clear alpha, scale and elevation, if it was changed in drag.
     */
    fun onClear(viewHolder: RecyclerView.ViewHolder?) {
        if (!isChanged) return

        isChanged = false

        val cardView = viewHolder?.itemView as? CardView ?: return

        AnimatorSet().apply {
            this.duration = ANIM_DURATION
            this.interpolator = AccelerateDecelerateInterpolator()

            playTogether(
                getAlphaAnimator(cardView, ALPHA_DRAG_MAX),
                getScaleXAnimator(cardView, SCALE_MIN),
                getScaleYAnimator(cardView, SCALE_MIN),
                getElevationAnimator(cardView, R.dimen.elevation_2dp)
            )
        }.start()
    }

    companion object {
        private const val ANIM_DURATION = 300L
        private const val ALPHA_DRAG_MIN = 0.7f
        private const val ALPHA_DRAG_MAX = 1f
        private const val SCALE_MIN = 1f
        private const val SCALE_MAX = 1.02f
    }
}