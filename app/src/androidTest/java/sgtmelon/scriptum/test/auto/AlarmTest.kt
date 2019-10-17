package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.extension.getString
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    // TODO #TEST setup for all tests?
    override fun setUp() {
        super.setUp()
        iPreferenceRepo.sort = Sort.CHANGE
    }

    @Test fun openTextNote() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote { onPressBack() } }.mainScreen() }
    }

    @Test fun openRollNote() = data.insertRoll().let {
        launchAlarm(it) { openAlarm(it) { openRollNote { onPressBack() } }.mainScreen() }
    }

    @Test fun clickDisable() = data.insertNote().let {
        launchAlarm(it) { openAlarm(it) { onClickDisable() }.mainScreen() }
    }


    @Test fun clickPostponeMin10() = startPostponeTest(Repeat.MIN_10)

    @Test fun clickPostponeMin30() = startPostponeTest(Repeat.MIN_30)

    @Test fun clickPostponeMin60() = startPostponeTest(Repeat.MIN_60)

    private fun startPostponeTest(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { onClickPostpone() }.mainScreen() }
        }
    }


    @Test fun clickPostponeDateExist() = data.insertText().let{
        iPreferenceRepo.repeat = Repeat.MIN_10

        launchAlarm(it) {
            val existDate = getTime(min = 10).getString()
            data.insertNotification(date = existDate)

            openAlarm(it, listOf(existDate)) { onClickPostpone() }
            mainScreen { notesScreen { openNotification { onAssertItem(1, it) } } }
        }
    }

    @Test fun clickPostponeCorrectSeconds() = data.insertText(data.textNote.copy(color = 1)).let {
        iPreferenceRepo.sort = Sort.COLOR
        iPreferenceRepo.repeat = Repeat.MIN_10

        val note = data.insertRoll(data.rollNote.copy(color = 0))

        launchAlarm(it) {
            openAlarm(it) { onClickPostpone() }
            mainScreen {
                notesScreen {
                    openNoteDialog(note) {
                        onNotification {
                            onClickApply(listOf(getTime(min = 10).getString())) {
                                onTime(min = 10).assert()
                            }
                        }
                    }
                }
            }
        }
    }


    @Test fun waitPostponeMin10() = startWaitPostponeTest(Repeat.MIN_10)

    @Test fun waitPostponeMin30() = startWaitPostponeTest(Repeat.MIN_30)

    @Test fun waitPostponeMin60() = startWaitPostponeTest(Repeat.MIN_60)

    private fun startWaitPostponeTest(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { waitPostpone() }.mainScreen() }
        }
    }


    @Test fun backPostponeMin10() = startBackPostponeTest(Repeat.MIN_10)

    @Test fun backPostponeMin30() = startBackPostponeTest(Repeat.MIN_30)

    @Test fun backPostponeMin60() = startBackPostponeTest(Repeat.MIN_60)

    private fun startBackPostponeTest(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { openAlarm(it) { onPressBack() }.mainScreen() }
        }
    }

}