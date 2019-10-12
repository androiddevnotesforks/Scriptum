package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    // TODO #TEST open note -> close by back press

    @Test fun openTextNote() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote() } }
    }

    @Test fun backTextNote() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote {  }} }
    }

    @Test fun openRollNote() = data.insertRoll().let {
        launchAlarm(it) { openAlarm(it) { openRollNote() } }
    }

    @Test fun clickDisable() = data.insertNote().let {
        launchAlarm(it) { openAlarm(it) { onClickDisable() } }
    }


    @Test fun clickPostponeMin10() = startPostponeTest(Repeat.MIN_10)

    @Test fun clickPostponeMin30() = startPostponeTest(Repeat.MIN_30)

    @Test fun clickPostponeMin60() = startPostponeTest(Repeat.MIN_60)

    private fun startPostponeTest(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let { launchAlarm(it) { openAlarm(it) { onClickPostpone() } } }
    }


    @Test fun waitPostponeMin10() = startWaitPostponeTest(Repeat.MIN_10)

    @Test fun waitPostponeMin30() = startWaitPostponeTest(Repeat.MIN_30)

    @Test fun waitPostponeMin60() = startWaitPostponeTest(Repeat.MIN_60)

    private fun startWaitPostponeTest(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { waitAfter(AlarmViewModel.CANCEL_DELAY) { openAlarm(it) } }
        }
    }


    @Test fun backPostponeMin10() = startBackPostponeTest(Repeat.MIN_10)

    @Test fun backPostponeMin30() = startBackPostponeTest(Repeat.MIN_30)

    @Test fun backPostponeMin60() = startBackPostponeTest(Repeat.MIN_60)

    private fun startBackPostponeTest(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let { launchAlarm(it) { openAlarm(it) { onPressClose() } } }
    }

}