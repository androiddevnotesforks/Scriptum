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
        val enterIcon: AnimatedVectorDrawable?,
        val exitIcon: AnimatedVectorDrawable?,
        private val changeCallback: IconChangeCallback
) {

    var blockCallback: IconBlockCallback? = null

    var animState: Boolean = false

    private val duration = context.resources.getInteger(R.integer.icon_animation_time).toLong()

    private val handler = Handler()
    private val runnable: Runnable = Runnable {
        if (enterIcon == null || exitIcon == null) return@Runnable

        if (enterIcon.isRunning || exitIcon.isRunning) {
            waitAnimationEnd()
        } else {
            blockCallback?.setEnabled(enabled = true)
            changeCallback.setDrawable(animState, needAnim = false)
        }
    }

    fun waitAnimationEnd() {
        blockCallback?.setEnabled(enabled = false)
        handler.postDelayed(runnable, duration)
    }

}