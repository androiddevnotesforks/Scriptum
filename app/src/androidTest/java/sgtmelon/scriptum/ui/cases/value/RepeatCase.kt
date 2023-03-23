package sgtmelon.scriptum.ui.cases.value

import android.content.res.Resources
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteAlarm
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
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

    /** Update [NoteAlarm.date] for [item]. Needed to display notification indicator in card. */
    fun setAlarm(item: NoteItem, repeat: Repeat, resources: Resources) {
        val minutes = resources.getIntArray(R.array.pref_alarm_repeat_array)[repeat.ordinal]
        item.alarm = NoteAlarm(date = getClearCalendar(minutes).toText())
    }
}