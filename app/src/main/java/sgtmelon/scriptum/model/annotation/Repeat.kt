package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Describes repeat in [PreferenceRepo.repeat]
 */
@IntDef(Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60)
annotation class Repeat {

    companion object {
        const val MIN_10 = 0
        const val MIN_30 = 1
        const val MIN_60 = 2
    }

}