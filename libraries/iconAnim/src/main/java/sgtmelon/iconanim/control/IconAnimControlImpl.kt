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
class IconAnimControlImpl(
    context: Context,
    private val enterIcon: AnimatedVectorDrawable?,
    private val exitIcon: AnimatedVectorDrawable?,
    private val changeCallback: IconChangeCallback,
    override var blockCallback: IconBlockCallback? = null
) : IconAnimControl {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    /**
     * Variable for prevent double call of [blockCallback]. Sometimes happen tests idling
     * crashes (timeout).
     *
     * Double call related with calls of [waitAnimationEnd] inside [checkAnimationEnd] and
     * [getIcon]. [isEnabled] - skip call if already set value inside [blockCallback].
     */
    private var isEnabled = true

    private val duration = context.resources.getInteger(R.integer.icon_animation_time).toLong()

    override fun getIcon(isEnterIcon: Boolean): AnimatedVectorDrawable? {
        val icon = if (isEnterIcon) enterIcon else exitIcon

        icon?.start()
        waitAnimationEnd(isEnterIcon)

        return icon
    }

    private fun waitAnimationEnd(isEnterIcon: Boolean) {
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
            waitAnimationEnd(isEnterIcon)
        } else {
            if (!isEnabled) {
                isEnabled = true
                blockCallback?.setEnabled(isEnabled = true)
            }

            changeCallback.setDrawable(isEnterIcon, needAnim = false)
        }
    }
}