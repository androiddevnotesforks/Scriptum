package sgtmelon.scriptum.test.ui.auto.screen.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getNewCalendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.IRepeatTest

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

    override fun startTest(@Repeat value: Int) {
        preferenceRepo.repeat = value

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { onClickRepeat() }.mainScreen() }
        }
    }

    @Test fun dateExist() = data.insertText().let {
        preferenceRepo.repeat = Repeat.MIN_10

        launchAlarm(it) {
            val existDate = getCalendarWithAdd(min = 10).getText()
            data.insertNotification(date = existDate)

            openAlarm(it, listOf(existDate)) { onClickRepeat() }
            mainScreen { notesScreen { openNotification { onAssertItem(1, it) } } }
        }
    }

    /**
     * Check reset seconds on click repeat button. And check alarm receiver work with notes screen.
     */
    @Test fun correctSeconds() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = listOf(
            Repeat.MIN_10, Repeat.MIN_30, Repeat.MIN_60,
            Repeat.MIN_180, Repeat.MIN_1440
        ).random()

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            var repeatCalendar = getNewCalendar()
            openAlarm(it) {
                repeatCalendar = onClickRepeat()
            }

            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(repeatCalendar.getText())) {
                                onTime(repeatCalendar).assert()
                            }
                        }
                    }
                }
            }
        }
    }

}