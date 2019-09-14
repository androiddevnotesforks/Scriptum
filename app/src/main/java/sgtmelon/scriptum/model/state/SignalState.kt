package sgtmelon.scriptum.model.state

import sgtmelon.scriptum.model.annotation.Signal
import sgtmelon.scriptum.room.converter.IntConverter

/**
 * State for control signal without use [IntConverter]
 */
class SignalState(val isMelody: Boolean, val isVibration: Boolean) {

    constructor(array: BooleanArray) : this(array[Signal.MELODY], array[Signal.VIBRATION])

}