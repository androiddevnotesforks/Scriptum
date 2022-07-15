package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.infrastructure.preferences.PreferenceRepo

/**
 * Describes signals in [PreferenceRepo.signal]
 */
@IntDef(Signal.MELODY, Signal.VIBRATION)
annotation class Signal {
    companion object {
        const val MELODY = 0
        const val VIBRATION = 1

        const val digitCount = 2
    }
}