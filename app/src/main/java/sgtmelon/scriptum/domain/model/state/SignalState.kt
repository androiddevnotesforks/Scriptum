package sgtmelon.scriptum.domain.model.state

import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.model.annotation.Signal

/**
 * State for control signal without use [IntConverter].
 */
data class SignalState(val isMelody: Boolean, val isVibration: Boolean) {

    companion object {
        operator fun get(array: BooleanArray): SignalState? {
            val isMelody = array.getOrNull(Signal.MELODY) ?: return null
            val isVibration = array.getOrNull(Signal.VIBRATION) ?: return null

            return SignalState(isMelody, isVibration)
        }
    }

}