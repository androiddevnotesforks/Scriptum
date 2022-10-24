package sgtmelon.scriptum.test.auto.other

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getCalendarWithAdd
import sgtmelon.extension.getText
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    @Test fun openTextNote() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote { onPressBack() } }.mainScreen() }
    }

    @Test fun openRollNote() = data.insertRoll().let {
        launchAlarm(it) { openAlarm(it) { openRollNote { onPressBack() } }.mainScreen() }
    }

    @Test fun clickDisable() = data.insertNote().let {
        launchAlarm(it) { openAlarm(it) { onClickDisable() }.mainScreen() }
    }


    @Test fun clickRepeatMin10() = startRepeatTest(Repeat.MIN_10)

    @Test fun clickRepeatMin30() = startRepeatTest(Repeat.MIN_30)

    @Test fun clickRepeatMin60() = startRepeatTest(Repeat.MIN_60)

    @Test fun clickRepeatMin180() = startRepeatTest(Repeat.MIN_180)

    @Test fun clickRepeatMin1440() = startRepeatTest(Repeat.MIN_1440)

    private fun startRepeatTest(@Repeat repeat: Int) {
        preferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { onClickRepeat() }.mainScreen() }
        }
    }


    @Test fun clickRepeatDateExist() = data.insertText().let{
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
    /**
     * androidx.test.espresso.base.DefaultFailureHandler$AssertionFailedWithCauseError: 'not is enabled' doesn't match the selected view.
     * Expected: not is enabled
     * Got: "AppCompatButton{id=16908313, res-name=button1, visibility=VISIBLE, width=66, height=54, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@1f5f96, tag=null, root-is-layout-requested=false, has-input-connection=false, x=89.0, y=4.0, text=Apply, input-type=0, ime-target=false, has-links=false}"
     */
    @Test fun clickRepeatCorrectSeconds() = data.insertText(data.textNote.copy(color = 1)).let {
        preferenceRepo.sort = Sort.COLOR
        preferenceRepo.repeat = Repeat.MIN_10

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            openAlarm(it) { onClickRepeat() }
            mainScreen {
                notesScreen {
                    onAssertItem(note, p = 0)
                    openNoteDialog(note, p = 0) {
                        onNotification {
                            onClickApply(listOf(getCalendarWithAdd(min = 10).getText())) {
                                onTime(min = 10).assert()
                            }
                        }
                    }
                }
            }
        }
    }


    @Test fun waitRepeatMin10() = startWaitRepeatTest(Repeat.MIN_10)

    @Test fun waitRepeatMin30() = startWaitRepeatTest(Repeat.MIN_30)

    @Test fun waitRepeatMin60() = startWaitRepeatTest(Repeat.MIN_60)

    @Test fun waitRepeatMin180() = startWaitRepeatTest(Repeat.MIN_180)

    @Test fun waitRepeatMin1440() = startWaitRepeatTest(Repeat.MIN_1440)

    private fun startWaitRepeatTest(@Repeat repeat: Int) {
        preferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { waitRepeat() }.mainScreen() }
        }
    }


    @Test fun backRepeatMin10() = startBackRepeatTest(Repeat.MIN_10)

    @Test fun backRepeatMin30() = startBackRepeatTest(Repeat.MIN_30)

    @Test fun backRepeatMin60() = startBackRepeatTest(Repeat.MIN_60)

    @Test fun backRepeatMin180() = startBackRepeatTest(Repeat.MIN_180)

    @Test fun backRepeatMin1440() = startBackRepeatTest(Repeat.MIN_1440)

    private fun startBackRepeatTest(@Repeat repeat: Int) {
        preferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { onPressBack() }.mainScreen() }
        }
    }


    @Test fun moreRepeatMin10() = startMoreRepeatTest(Repeat.MIN_10)

    @Test fun moreRepeatMin30() = startMoreRepeatTest(Repeat.MIN_30)

    @Test fun moreRepeatMin60() = startMoreRepeatTest(Repeat.MIN_60)

    @Test fun moreRepeatMin180() = startMoreRepeatTest(Repeat.MIN_180)

    @Test fun moreRepeatMin1440() = startMoreRepeatTest(Repeat.MIN_1440)

    private fun startMoreRepeatTest(@Repeat repeat: Int) = data.insertNote().let {
        launchAlarm(it) { openAlarm(it) { openMoreDialog { onClickRepeat(repeat) } }.mainScreen() }
    }

    @Test fun moreDialogUse()  = data.insertNote().let {
        launchAlarm(it) {
            openAlarm(it) {
                openMoreDialog { onCloseSoft() }.assert()
                openMoreDialog { onCloseSwipe() }.assert()
            }
        }
    }

}