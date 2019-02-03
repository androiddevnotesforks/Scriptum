package sgtmelon.iconanim.library

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi

/**
 * Handler для регистрации начала и конца анимации
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class IconAnimUtil(context: Context,
                   val animOn: AnimatedVectorDrawable,
                   val animOff: AnimatedVectorDrawable,
                   private val iconAnimControl: IconAnimControl) {

    private val animTime: Int = context.resources.getInteger(android.R.integer.config_shortAnimTime)

    private val animHandler = Handler()

    private val animRunnable: Runnable = Runnable {
        if (animOn.isRunning || animOff.isRunning) {
            waitAnimationEnd()
        } else {
            iconAnimControl.setDrawable(animState, false)
        }
    }

    var animState: Boolean = false

    fun waitAnimationEnd() {
        animHandler.postDelayed(animRunnable, animTime.toLong())
    }

}