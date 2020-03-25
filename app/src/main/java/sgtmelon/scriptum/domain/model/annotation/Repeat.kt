package sgtmelon.scriptum.domain.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo

/**
 * Describes repeat in [PreferenceRepo.repeat]
 */
@IntDef(Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60, Repeat.MIN_180, Repeat.MIN_1440)
annotation class Repeat {

    companion object {
        const val MIN_10 = 0
        const val MIN_30 = 1
        const val MIN_60 = 2
        const val MIN_180 = 3
        const val MIN_1440 = 4
    }

}