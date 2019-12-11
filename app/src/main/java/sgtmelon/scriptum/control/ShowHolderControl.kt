package sgtmelon.scriptum.control

import android.os.Handler
import android.view.View
import androidx.annotation.IntegerRes
import sgtmelon.scriptum.R

/**
 * Class for help control showing placeholders while transition happen.
 */
class ShowHolderControl(
        private val viewArray: Array<View?>,
        @IntegerRes private val time: Int = R.integer.fade_anim_time
) {

    private val timeValue = viewArray.random()?.context?.resources?.getInteger(time)?.toLong() ?: 0L

    private val handler = Handler()
    private val runnable = { viewArray.forEach { it?.visibility = View.INVISIBLE } }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
    }

    fun show() {
        viewArray.forEach { it?.visibility = View.VISIBLE }

        handler.removeCallbacksAndMessages(null)
        handler.postDelayed(runnable, timeValue)
    }

}