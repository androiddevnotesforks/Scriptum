package sgtmelon.scriptum.ui.auto.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extensions.getCalendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.RepeatCase

/**
 * Test for [AlarmActivity].
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepeatClickTest : ParentUiTest(), RepeatCase {

    @Test override fun repeatMin10() = super.repeatMin10()

    @Test override fun repeatMin30() = super.repeatMin30()

    @Test override fun repeatMin60() = super.repeatMin60()

    @Test override fun repeatMin180() = super.repeatMin180()

    @Test override fun repeatMin1440() = super.repeatMin1440()

    override fun startTest(value: Repeat) {
        preferencesRepo.repeat = value
        startAlarmCloseTest(db.insertNote()) { repeat() }
    }

    @Test fun dateExist() = db.insertText().let {
        preferencesRepo.repeat = Repeat.MIN_10

        launchSplashAlarm(it) {
            val existDate = getClearCalendar(addMinutes = 10).toText()
            db.insertNotification(date = existDate)

            alarmScreen(it, listOf(existDate)) { repeat() }
            mainScreen { openNotes { openNotifications { assertItem(it, 1) } } }
        }
    }

    /**
     * Check reset seconds after clicking of repeat button. And check [NoteItemUi] indicator
     * after setting up new notification (repeat click).
     */
    @Test fun correctSeconds() = db.insertText(db.textNote.copy(color = Color.PURPLE)).let {
        preferencesRepo.sort = Sort.COLOR
        preferencesRepo.repeat = Repeat.values().random()

        val note = db.insertRoll(db.rollNote.copy(color = Color.RED))

        launchSplashAlarm(it) {
            var repeatCalendar = getCalendar()
            alarmScreen(it) {
                repeatCalendar = repeat()
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