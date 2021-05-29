package sgtmelon.scriptum.test.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.SignalDialogUi


/**
 * Test for [AlarmPreferenceFragment] and [SignalDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceSignalTest : ParentUiTest(), IAlarmPreferenceTest {

    @Test fun dialogClose() = runTest {
        openSignalDialog { onCloseSoft() }
        assert()
        openSignalDialog { onClickCancel() }
        assert()
    }

    @Test fun dialogWork() = runTest({
        getLogic().alarmInteractor.updateSignal(booleanArrayOf(true, true))
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