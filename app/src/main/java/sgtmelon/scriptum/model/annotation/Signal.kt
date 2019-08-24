package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Describes signals in [PreferenceRepo.signalCheck]
 */
@IntDef(Signal.MELODY, Signal.VIBRATION)
annotation class Signal {

    companion object {
        const val MELODY = 0
        const val VIBRATION = 1

        const val digitCount = 2
    }

}