package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.basic.waitAfter

/**
 * Test for [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class AlarmTest : ParentUiTest() {

    @Test fun openTextNote() = data.insertText().let {
        launchAlarm(it) { openAlarm(it) { openTextNote() } }
    }

    @Test fun openRollNote() = data.insertRoll().let {
        launchAlarm(it) { openAlarm(it) { openRollNote() } }
    }

    @Test fun clickDisable() = data.insertNote().let {
        launchAlarm(it) { openAlarm(it) { onClickDisable() } }
    }


    @Test fun clickPostponeMin10() = clickPostpone(Repeat.MIN_10)

    @Test fun clickPostponeMin30() = clickPostpone(Repeat.MIN_30)

    @Test fun clickPostponeMin60() = clickPostpone(Repeat.MIN_60)


    @Test fun waitPostponeMin10() = waitPostpone(Repeat.MIN_10)

    @Test fun waitPostponeMin30() = waitPostpone(Repeat.MIN_30)

    @Test fun waitPostponeMin60() = waitPostpone(Repeat.MIN_60)


    @Test fun backPostponeMin10() = backPostpone(Repeat.MIN_10)

    @Test fun backPostponeMin30() = backPostpone(Repeat.MIN_30)

    @Test fun backPostponeMin60() = backPostpone(Repeat.MIN_60)


    private fun clickPostpone(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let { launchAlarm(it) { openAlarm(it) { onClickPostpone() } } }
    }

    private fun waitPostpone(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let {
            launchAlarm(it) { waitAfter(AlarmViewModel.CANCEL_DELAY) { openAlarm(it) } }
        }
    }

    private fun backPostpone(@Repeat repeat: Int) {
        iPreferenceRepo.repeat = repeat

        data.insertNote().let { launchAlarm(it) { openAlarm(it) { onPressBack() } } }
    }

}