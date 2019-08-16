package sgtmelon.scriptum.control.alarm

import android.content.Context
import sgtmelon.scriptum.control.alarm.callback.IFlashlightControl

/**
 * Class for help control []
 *
 * @author SerjantArbuz
 */
class FlashlightControl(context: Context): IFlashlightControl {

    companion object {
        fun getInstance(context: Context): IFlashlightControl = FlashlightControl(context)
    }

}