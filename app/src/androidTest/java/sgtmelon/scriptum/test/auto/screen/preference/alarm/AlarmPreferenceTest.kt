package sgtmelon.scriptum.test.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.logic.preference.AlarmPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.AlarmPreferenceScreen

/**
 * Test for [AlarmPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceTest : ParentUiTest() {

    private val logic = AlarmPreferenceLogic()

    private fun runTest(before: () -> Unit = {}, func: AlarmPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openAlarm(func) } } }
        }
    }

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertMelody() = startAssertTest(isMelody = true, isVibration = false)

    @Test fun assertVibration() = startAssertTest(isMelody = false, isVibration = true)

    @Test fun assertBoth() = startAssertTest(isMelody = true, isVibration = true)

    private fun startAssertTest(isMelody: Boolean, isVibration: Boolean) = runTest({
        logic.interactor.updateSignal(booleanArrayOf(isMelody, isVibration))
    }) { assert() }

}