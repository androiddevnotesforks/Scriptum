package sgtmelon.scriptum.infrastructure.adapter.animation

import android.animation.AnimatorSet
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.ALPHA_MAX
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.getElevationAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.getScaleXAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.getScaleYAnimator

class TouchAnimation {

    private var isChanged = false
    private var startCardElevation: Float = 0f

    /** Change alpha, scale and elevation on drag start. */
    fun onDrag(viewHolder: RecyclerView.ViewHolder?) {
        val cardView = viewHolder?.itemView as? CardView ?: return
        val duration = cardView.context.resources.getInteger(R.integer.touch_change_time).toLong()

        startCardElevation = cardView.cardElevation

        AnimatorSet().apply {
            this.duration = duration
            this.interpolator = AccelerateDecelerateInterpolator()

            playTogether(
                cardView.getAlphaAnimator(ALPHA_DRAG_MIN),
                cardView.getScaleXAnimator(SCALE_MAX),
                cardView.getScaleYAnimator(SCALE_MAX),
                cardView.getElevationAnimator(R.dimen.item_card_drag_elevation)
            )
        }.start()

        isChanged = true
    }

    /** Clear alpha, scale and elevation, if it was changed in drag. */
    fun onClear(viewHolder: RecyclerView.ViewHolder?) {
        if (!isChanged) return

        isChanged = false

        val cardView = viewHolder?.itemView as? CardView ?: return
        val duration = cardView.context.resources.getInteger(R.integer.touch_change_time).toLong()

        AnimatorSet().apply {
            this.duration = duration
            this.interpolator = AccelerateDecelerateInterpolator()

            playTogether(
                cardView.getAlphaAnimator(ALPHA_MAX),
                cardView.getScaleXAnimator(SCALE_MIN),
                cardView.getScaleYAnimator(SCALE_MIN),
                cardView.getElevationAnimator(startCardElevation)
            )
        }.start()
    }

    companion object {
        private const val ALPHA_DRAG_MIN = 0.7f
        private const val SCALE_MIN = 1f
        private const val SCALE_MAX = 1.015f
    }
}