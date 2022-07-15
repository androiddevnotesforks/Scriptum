package sgtmelon.scriptum.test.parent.situation

import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat

/**
 * Interface describes [Repeat] tests.
 */
interface IRepeatTest {

    fun repeatMin10() = startTest(Repeat.MIN_10)

    fun repeatMin30() = startTest(Repeat.MIN_30)

    fun repeatMin60() = startTest(Repeat.MIN_60)

    fun repeatMin180() = startTest(Repeat.MIN_180)

    fun repeatMin1440() = startTest(Repeat.MIN_1440)

    fun startTest(@Repeat value: Int)

}