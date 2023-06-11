package sgtmelon.scriptum.infrastructure.widgets

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import sgtmelon.scriptum.R
import sgtmelon.test.idling.getWaitIdling

/**
 * The same [ProgressBar], but with smooth progress change animation.
 */
class SmoothProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ProgressBar(context, attrs, defStyleAttr, defStyleRes) {

    var skipFirstAnimation = true

    fun getProgressSmooth(): Int = progress / ANIM_SCALE

    fun setProgressSmooth(max: Int, progress: Int) {
        if (this.max == 0 && this.progress == 0 && skipFirstAnimation) {
            setMax(max * ANIM_SCALE)
            setProgress(progress * ANIM_SCALE)
        } else {
            setMax(max * ANIM_SCALE)
            setProgressSmooth(progress)
        }
    }

    fun setProgressSmooth(progress: Int) {
        if (progress == getProgressSmooth()) return

        val duration = resources.getInteger(R.integer.progress_change_time).toLong()
        val valueTo = progress * ANIM_SCALE

        ObjectAnimator.ofInt(this, "progress", valueTo).apply {
            this.duration = duration
            this.interpolator = AccelerateDecelerateInterpolator()
        }.start()

        getWaitIdling().start(duration)
    }

    companion object {

        /**
         * Variable for increase [setMax] and [setProgress] values, and change of them will
         * look smoother.
         */
        const val ANIM_SCALE = 2500
    }
}