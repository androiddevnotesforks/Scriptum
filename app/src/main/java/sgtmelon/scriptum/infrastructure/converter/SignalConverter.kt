package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.preference.Signal
import sgtmelon.scriptum.infrastructure.model.state.SignalState

class SignalConverter {

    fun toState(value: String): SignalState? {
        val array = toArray(value)

        val isMelody = array.getOrNull(Signal.MELODY.ordinal) ?: return null
        val isVibration = array.getOrNull(Signal.VIBRATION.ordinal) ?: return null

        return SignalState(isMelody, isVibration)
    }

    fun toString(value: BooleanArray): String {
        return value.asSequence()
            .mapIndexed { i, b -> if (b) Signal.values().getOrNull(i)?.ordinal else null }
            .filterNotNull()
            .joinToString(DIVIDER)
    }

    fun toArray(value: String): BooleanArray {
        val array = BooleanArray(Signal.values().size)
        val valueList = value.split(DIVIDER)

        for ((i, item) in Signal.values().withIndex()) {
            array[i] = valueList.any { it == item.ordinal.toString() }
        }

        return array
    }

    companion object {
        private const val DIVIDER = ", "
    }
}