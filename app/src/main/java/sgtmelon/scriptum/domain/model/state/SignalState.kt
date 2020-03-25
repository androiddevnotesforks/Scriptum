package sgtmelon.scriptum.domain.model.state

import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.model.annotation.Signal

/**
 * State for control signal without use [IntConverter]
 */
class SignalState(val isMelody: Boolean, val isVibration: Boolean) {

    constructor(array: BooleanArray) : this(array[Signal.MELODY], array[Signal.VIBRATION])

}