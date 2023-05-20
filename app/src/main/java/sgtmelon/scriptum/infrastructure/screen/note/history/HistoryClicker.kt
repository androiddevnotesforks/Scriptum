package sgtmelon.scriptum.infrastructure.screen.note.history

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import sgtmelon.extensions.launchBack
import sgtmelon.extensions.runMain
import sgtmelon.scriptum.infrastructure.factory.SystemDelegatorFactory
import kotlin.math.max

/**
 * Class for [View.callOnClick] multiple times (for undo/redo button) with decreasing delay each
 * iteration.
 */
class HistoryClicker(
    private val lifecycleScope: CoroutineScope,
    private val getSystem: () -> SystemDelegatorFactory?
) {

    fun repeat(button: View) = repeat(button, iteration = 0)

    private fun repeat(button: View, iteration: Int) {
        if (!button.isPressed || !button.isEnabled) return

        lifecycleScope.launchBack {
            runMain {
                button.callOnClick()
                getSystem()?.vibrator?.startShort()
            }

            val gap = HISTORY_GAP_MAX - iteration * HISTORY_GAP_STEP
            delay(max(gap, HISTORY_GAP_MIN))

            repeat(button, iteration = iteration + 1)
        }
    }

    companion object {
        const val HISTORY_GAP_MIN = 100L
        const val HISTORY_GAP_MAX = 300L
        const val HISTORY_GAP_STEP = 30L
    }
}