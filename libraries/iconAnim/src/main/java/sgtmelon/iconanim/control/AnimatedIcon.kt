package sgtmelon.iconanim.control

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.extensions.runMain
import sgtmelon.iconanim.R
import sgtmelon.iconanim.callback.IconBlockCallback
import sgtmelon.iconanim.callback.IconChangeCallback

/**
 * Control class for register animation vector start/end.
 */
class AnimatedIcon(
    context: Context,
    private val enterIcon: AnimatedVectorDrawable?,
    private val exitIcon: AnimatedVectorDrawable?,
    private val changeCallback: IconChangeCallback,
    private var blockCallback: IconBlockCallback? = null
) {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    /**
     * Variable for prevent double call of [blockCallback]. Sometimes happen tests idling
     * crashes (timeout).
     *
     * Double call related with calls of [launchSimpleIconSet] inside [checkAnimationEnd] and
     * [getAndStart]. [isEnabled] - skip call if already set value inside [blockCallback].
     */
    private var isEnabled = true

    private val duration = context.resources.getInteger(R.integer.icon_animation_time).toLong()

    fun setBlockCallback(callback: IconBlockCallback) {
        this.blockCallback = callback
    }

    fun getAndStart(isEnterIcon: Boolean): AnimatedVectorDrawable? {
        val icon = if (isEnterIcon) enterIcon else exitIcon

        icon?.start()
        launchSimpleIconSet(isEnterIcon)

        return icon
    }

    private fun launchSimpleIconSet(isEnterIcon: Boolean) {
        if (isEnabled) {
            isEnabled = false
            blockCallback?.setEnabled(isEnabled = false)
        }

        ioScope.launch {
            delay(duration)
            runMain { checkAnimationEnd(isEnterIcon) }
        }
    }

    private fun checkAnimationEnd(isEnterIcon: Boolean) {
        if (enterIcon == null || exitIcon == null) return

        if (enterIcon.isRunning || exitIcon.isRunning) {
            launchSimpleIconSet(isEnterIcon)
        } else {
            if (!isEnabled) {
                isEnabled = true
                blockCallback?.setEnabled(isEnabled = true)
            }

            changeCallback.setDrawable(isEnterIcon, needAnim = false)
        }
    }
}