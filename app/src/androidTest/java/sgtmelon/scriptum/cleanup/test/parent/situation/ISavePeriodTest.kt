package sgtmelon.scriptum.cleanup.test.parent.situation

import sgtmelon.scriptum.infrastructure.model.key.SavePeriod

/**
 * Interface describes [SavePeriod] tests.
 */
interface ISavePeriodTest {

    fun savePeriodMin1() = startText(SavePeriod.MIN_1)

    fun savePeriodMin3() = startText(SavePeriod.MIN_3)

    fun savePeriodMin7() = startText(SavePeriod.MIN_7)

    fun startText(value: SavePeriod)
}