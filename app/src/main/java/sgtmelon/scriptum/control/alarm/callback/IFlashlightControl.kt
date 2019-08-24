package sgtmelon.scriptum.control.alarm.callback

import sgtmelon.scriptum.control.alarm.FlashlightControl

/**
 * Interface for [FlashlightControl]
 *
 * @author SerjantArbuz
 */
interface IFlashlightControl {

    fun toggle()

    fun turnOn()

    fun turnOff()

}