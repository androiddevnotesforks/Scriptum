package sgtmelon.scriptum.cleanup.presentation.control.toolbar.show

import android.os.Handler
import android.view.View
import androidx.annotation.IntegerRes
import sgtmelon.scriptum.R

/**
 * Class for help control showing placeholders while transition happen.
 */
// TODO refactor + apply DelayJobDelegator with subscribe on lifecycle
class HolderShowControl(
    private val viewArray: Array<View?>,
    @IntegerRes private val time: Int = R.integer.placeholder_fade_time
) : IHolderShowControl {

    // TODO what the fuck?) why use random? may be provide context/resources for
    private val timeValue = viewArray.random()?.context?.resources?.getInteger(time)?.toLong() ?: 0L

    private val handler = Handler()
    private val runnable = { for (it in viewArray) it?.visibility = View.INVISIBLE }

    override fun display() {
        for (it in viewArray) {
            it?.visibility = View.VISIBLE
        }

        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(runnable, timeValue)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        operator fun get(vararg view: View?): IHolderShowControl {
            return HolderShowControl(arrayOf(*view))
        }
    }
}