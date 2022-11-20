package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.dialog.preference.SignalDialogUi
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest


/**
 * Test for [AlarmPreferenceFragment] and [SignalDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceSignalTest : ParentUiTest(), IAlarmPreferenceTest {

    @Test fun dialogClose() = runTest {
        openSignalDialog { softClose() }
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

        assertTrue(preferencesRepo.signalTypeCheck.contentEquals(value))
    }

    /**
     * Switch signal to another one.
     */
    private fun switchValue(value: BooleanArray): BooleanArray {
        var initValue: BooleanArray

        do {
            initValue = getLogic().getRandomSignal()
            preferencesRepo.signalTypeCheck = initValue
        } while (initValue.contentEquals(value))

        return initValue
    }
}