package sgtmelon.scriptum.ui.cases.value

import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat


/**
 * Interface describes [Repeat] tests.
 */
interface RepeatCase {

    fun repeatMin10() = startTest(Repeat.MIN_10)

    fun repeatMin30() = startTest(Repeat.MIN_30)

    fun repeatMin60() = startTest(Repeat.MIN_60)

    fun repeatMin180() = startTest(Repeat.MIN_180)

    fun repeatMin1440() = startTest(Repeat.MIN_1440)

    fun startTest(value: Repeat)

}