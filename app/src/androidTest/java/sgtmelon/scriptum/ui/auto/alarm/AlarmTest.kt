package sgtmelon.scriptum.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchAlarm
import sgtmelon.scriptum.parent.ui.tests.launchAlarmClose
import sgtmelon.scriptum.ui.cases.NoteOpenCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

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
            smallLongPressTime {
                noteLongClick(it)
            }
            assert()
        }
    }

    @Test fun disable() = launchAlarmClose(db.insertNote()) { disable() }
}