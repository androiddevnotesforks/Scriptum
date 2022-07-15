package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef

/**
 * Describes identifiers for auto save period
 */
@IntDef(SavePeriod.MIN_1, SavePeriod.MIN_3, SavePeriod.MIN_7)
annotation class SavePeriod {
    companion object {
        const val MIN_1 = 0
        const val MIN_3 = 1
        const val MIN_7 = 2

        val list = listOf(MIN_1, MIN_3, MIN_7)
    }
}