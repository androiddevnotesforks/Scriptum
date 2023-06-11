package sgtmelon.scriptum.source.cases.value

import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod

/**
 * Interface describes [SavePeriod] tests.
 */
interface SavePeriodCase {

    fun savePeriodMin1() = startText(SavePeriod.MIN_1)

    fun savePeriodMin3() = startText(SavePeriod.MIN_3)

    fun savePeriodMin7() = startText(SavePeriod.MIN_7)

    fun startText(value: SavePeriod)
}