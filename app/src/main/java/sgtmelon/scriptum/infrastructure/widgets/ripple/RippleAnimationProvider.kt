package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.StringDef

/**
 * Class for working with ripple animation.
 */
class RippleAnimationProvider(private val settings: RippleSettings) {

    fun getLogoAnimators(hookView: View): List<ObjectAnimator> = with(settings) {
        return listOf(
            hookView.getAnimator(Anim.SCALE_X, logoDelay, delay, *logoPulse),
            hookView.getAnimator(Anim.SCALE_Y, logoDelay, delay, *logoPulse)
        )
    }

    fun getItemAnimators(view: View, position: Int): List<ObjectAnimator> = with(settings) {
        val delay = settings.getDelay(position)

        return listOf(
            view.getAnimator(Anim.SCALE_X, delay, duration, scaleFrom, scaleTo),
            view.getAnimator(Anim.SCALE_Y, delay, duration, scaleFrom, scaleTo),
            view.getAnimator(Anim.ALPHA, delay, duration, alphaFrom, alphaTo)
        )
    }

    @StringDef(Anim.SCALE_X, Anim.SCALE_Y, Anim.ALPHA)
    private annotation class Anim {
        companion object {
            const val SCALE_X = "ScaleX"
            const val SCALE_Y = "ScaleY"
            const val ALPHA = "Alpha"
        }
    }

    private fun View.getAnimator(
        @Anim animName: String,
        startDelay: Long,
        duration: Long,
        vararg values: Float
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(this, animName, *values).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART

            /**
             * Strange bug without 'when' and with lift return (try replace with 'if'
             * and you will see).
             */
            @Suppress("LiftReturnOrAssignment")
            when (animName) {
                Anim.ALPHA -> interpolator = DecelerateInterpolator()
                else -> interpolator = AccelerateDecelerateInterpolator()
            }

            this.startDelay = startDelay
            this.duration = duration
        }
    }
}