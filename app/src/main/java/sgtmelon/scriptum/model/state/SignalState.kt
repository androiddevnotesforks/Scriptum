package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.model.annotation.Signal
import sgtmelon.scriptum.repository.preference.IPreferenceRepo

/**
 * State for control signal without use [IPreferenceRepo.signalCheck]
 *
 * @author SerjantArbuz
 */
class SignalState(val isMelody: Boolean, val isVibration: Boolean, val isFlashlight: Boolean) {

    constructor(array: BooleanArray) :
            this(array[Signal.MELODY], array[Signal.VIBRATION], array[Signal.FLASHLIGHT])

}