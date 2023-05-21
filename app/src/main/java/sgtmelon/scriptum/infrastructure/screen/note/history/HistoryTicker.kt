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
class HistoryTicker(
    private val lifecycleScope: CoroutineScope,
    private val getSystem: () -> SystemDelegatorFactory?
) {

    fun start(button: View, onTick: () -> Unit, onFinish: () -> Unit) =
        start(button, onTick, onFinish, START_ITERATION)

    private fun start(button: View, onTick: () -> Unit, onFinish: () -> Unit, iteration: Int) {
        if (!button.isPressed || !button.isEnabled) {
            onFinish()
            return
        }

        lifecycleScope.launchBack {
            runMain {
                getSystem()?.vibrator?.startShort()
                onTick()
            }

            val gap = HISTORY_GAP_MAX - iteration * HISTORY_GAP_STEP
            delay(max(gap, HISTORY_GAP_MIN))

            start(button, onTick, onFinish, iteration = iteration + 1)
        }
    }

    companion object {
        private const val START_ITERATION = 0

        const val HISTORY_GAP_MIN = 150L
        const val HISTORY_GAP_MAX = 325L
        const val HISTORY_GAP_STEP = 25L
    }
}