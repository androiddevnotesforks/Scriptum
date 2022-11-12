package sgtmelon.scriptum.cleanup.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extensions.getCalendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.test.parent.situation.IRepeatTest
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmClickRepeatTest : ParentUiTest(), IRepeatTest {

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(value: Repeat) {
        preferencesRepo.repeat = value

        db.insertNote().let {
            launchAlarm(it) { alarmScreen(it) { onClickRepeat() }.mainScreen() }
        }
    }

    @Test fun dateExist() = db.insertText().let {
        preferencesRepo.repeat = Repeat.MIN_10

        launchAlarm(it) {
            val existDate = getClearCalendar(addMinutes = 10).toText()
            db.insertNotification(date = existDate)

            alarmScreen(it, listOf(existDate)) { onClickRepeat() }
            mainScreen { openNotes { openNotifications { assertItem(it, 1) } } }
        }
    }

    /**
     * Check reset seconds on click repeat button. And check alarm receiver work with notes screen.
     */
    @Test fun correctSeconds() = db.insertText(db.textNote.copy(color = Color.PURPLE)).let {
        preferencesRepo.sort = Sort.COLOR
        preferencesRepo.repeat = Repeat.values().random()

        val note = db.insertRoll(db.rollNote.copy(color = Color.RED))

        launchAlarm(it) {
            var repeatCalendar = getCalendar()
            alarmScreen(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                openNotes {
                    assertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        notification {
                            applyDate(listOf(repeatCalendar.toText())) {
                                set(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }
}