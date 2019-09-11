package sgtmelon.iconanim

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi

/**
 * Handler for register animation start/end
 *
 * @author SerjantArbuz
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class IconAnimControl(
        context: Context,
        val iconAnimOn: AnimatedVectorDrawable?,
        val iconAnimOff: AnimatedVectorDrawable?,
        private val callback: Callback
) {

    private val animTime: Int = context.resources.getInteger(android.R.integer.config_shortAnimTime)

    private val animHandler = Handler()

    private val animRunnable: Runnable = Runnable {
        if (iconAnimOn == null || iconAnimOff == null) return@Runnable

        if (iconAnimOn.isRunning || iconAnimOff.isRunning) {
            waitAnimationEnd()
        } else {
            callback.setDrawable(animState, needAnim = false)
        }
    }

    var animState: Boolean = false

    fun waitAnimationEnd() {
        animHandler.postDelayed(animRunnable, animTime.toLong())
    }

    interface Callback {
        fun setDrawable(drawableOn: Boolean, needAnim: Boolean)
    }

}