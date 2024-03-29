package sgtmelon.scriptum.tests.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchAlarm
import sgtmelon.scriptum.source.ui.tests.launchAlarmClose
import sgtmelon.scriptum.source.cases.list.ListScrollCase
import sgtmelon.scriptum.source.cases.note.NoteOpenCase

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest(),
    ListScrollCase,
    NoteOpenCase {

    @Test override fun listScroll() = launchAlarm(db.insertNote()) { scrollThrough() }

    @Test override fun itemTextOpen() = launchAlarmClose(db.insertText()) {
        openText(it) { pressBack() }
    }

    @Test override fun itemRollOpen() = launchAlarmClose(db.insertRoll()) {
        openRoll(it) { pressBack() }
    }

    @Test fun itemLongClick() {
        launchAlarm(db.insertNote()) {
            noteLongClick(it)
            assert()
        }
    }

    @Test fun disable() = launchAlarmClose(db.insertNote()) { disable() }
}