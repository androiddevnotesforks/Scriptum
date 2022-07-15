package sgtmelon.scriptum.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
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

    @Test fun dialogWork() {
        val value = getLogic().getRandomSignal()
        val initValue = switchValue(value)

        assertFalse(initValue.contentEquals(value))
        assertEquals(initValue.size, value.size)

        runTest {
            openSignalDialog {
                onClickItem(value).onClickItem(initValue).onClickItem(value).onClickApply()
            }
            assert()
        }

        assertTrue(getLogic().signalInteractor.typeCheck.contentEquals(value))
    }

    /**
     * Switch signal to another one.
     */
    private fun switchValue(value: BooleanArray): BooleanArray {
        var initValue: BooleanArray

        do {
            initValue = getLogic().getRandomSignal()
            getLogic().alarmInteractor.updateSignal(initValue)
        } while (getLogic().signalInteractor.typeCheck.contentEquals(value))

        return initValue
    }
}