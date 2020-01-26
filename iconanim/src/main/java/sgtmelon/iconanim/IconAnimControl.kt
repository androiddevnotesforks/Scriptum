package sgtmelon.iconanim

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi

/**
 * Handler for register animation start/end
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class IconAnimControl(
        context: Context,
        val iconOn: AnimatedVectorDrawable?,
        val iconOff: AnimatedVectorDrawable?,
        private val callback: IconCallback
) {

    var animState: Boolean = false

    private val animationTime = context.resources.getInteger(R.integer.icon_animation_time).toLong()

    private val animHandler = Handler()
    private val animRunnable: Runnable = Runnable {
        if (iconOn == null || iconOff == null) return@Runnable

        if (iconOn.isRunning || iconOff.isRunning) {
            waitAnimationEnd()
        } else {
            callback.setEnabled(enabled = true)
            callback.setDrawable(animState, needAnim = false)
        }
    }

    fun waitAnimationEnd() {
        callback.setEnabled(enabled = false)
        animHandler.postDelayed(animRunnable, animationTime)
    }

}