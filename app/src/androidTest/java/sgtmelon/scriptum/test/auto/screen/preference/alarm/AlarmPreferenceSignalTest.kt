package sgtmelon.scriptum.test.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi
import sgtmelon.scriptum.ui.logic.preference.AlarmPreferenceLogic
import sgtmelon.scriptum.ui.screen.preference.AlarmPreferenceScreen


/**
 * Test for [AlarmPreferenceFragment] and [RepeatDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceSignalTest : ParentUiTest() {

    private val logic = AlarmPreferenceLogic()

    private fun runTest(before: () -> Unit = {}, func: AlarmPreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openAlarm(func) } } }
        }
    }

    @Test fun dialogClose() = runTest {
        openSignalDialog { onCloseSoft() }
        assert()
        openSignalDialog { onClickCancel() }
        assert()
    }

    @Test fun dialogWork() = runTest({
        logic.alarmInteractor.updateSignal(booleanArrayOf(true, true))
    }) {
        openSignalDialog {
            repeat(times = 2) {
                onClickItem(position = 0)
                onClickItem(position = 1)
            }
            onClickItem((0..1).random())
            onClickApply()
        }
        assert()
    }
}