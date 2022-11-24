package sgtmelon.scriptum.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.NoteOpenCase
import sgtmelon.scriptum.ui.cases.list.ListScrollCase

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest(),
    ListScrollCase,
    NoteOpenCase {

    @Test override fun listScroll() = startAlarmTest(db.insertNote()) { scrollThrough() }

    @Test override fun itemTextOpen() = startAlarmCloseTest(db.insertText()) {
        openText(it) { pressBack() }
    }

    @Test override fun itemRollOpen() = startAlarmCloseTest(db.insertRoll()) {
        openRoll(it) { pressBack() }
    }

    @Test fun itemLongClick() {
        startAlarmTest(db.insertNote()) {
            smallLongPressTime {
                noteLongClick(it)
            }
            assert()
        }
    }

    @Test fun disable() = startAlarmCloseTest(db.insertNote()) { disable() }
}