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
    private val enterIcon: AnimatedVectorDrawable?,
    private val exitIcon: AnimatedVectorDrawable?,
    private val changeCallback: IconChangeCallback
) {

    var blockCallback: IconBlockCallback? = null

    /**
     * Variable for save icon state.
     */
    private var isEnterIcon: Boolean = false

    /**
     * Variable for prevent double call of [blockCallback]. Because happen tests idling crashes.
     * Double call happen because of calls [waitAnimationEnd] inside [runnable] and [getIcon].
     */
    private var isEnabled = true

    private val duration = context.resources.getInteger(R.integer.icon_animation_time).toLong()

    private val handler = Handler()
    private val runnable: Runnable = Runnable {
        if (enterIcon == null || exitIcon == null) return@Runnable

        if (enterIcon.isRunning || exitIcon.isRunning) {
            waitAnimationEnd()
        } else {
            if (!isEnabled) {
                isEnabled = true
                blockCallback?.setEnabled(isEnabled)
            }

            changeCallback.setDrawable(isEnterIcon, needAnim = false)
        }
    }

    fun getIcon(isEnterIcon: Boolean): AnimatedVectorDrawable? {
        this.isEnterIcon = isEnterIcon

        val icon = if (isEnterIcon) enterIcon else exitIcon

        icon?.start()
        waitAnimationEnd()

        return icon
    }

    private fun waitAnimationEnd() {
        if (isEnabled) {
            isEnabled = false
            blockCallback?.setEnabled(isEnabled)
        }

        handler.postDelayed(runnable, duration)
    }
}