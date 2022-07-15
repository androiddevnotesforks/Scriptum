package sgtmelon.scriptum.test.parent.situation

import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod

/**
 * Interface describes [SavePeriod] tests.
 */
interface ISavePeriodTest {

    fun savePeriodMin1() = startText(SavePeriod.MIN_1)

    fun savePeriodMin3() = startText(SavePeriod.MIN_3)

    fun savePeriodMin7() = startText(SavePeriod.MIN_7)

    fun startText(@SavePeriod value: Int)

}