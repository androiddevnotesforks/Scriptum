package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SignalDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.utils.getRandomSignalCheck
import sgtmelon.scriptum.ui.cases.DialogCloseCase


/**
 * Test for [AlarmPreferenceFragment] and [SignalDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceSignalTest : ParentUiTest(),
    DialogCloseCase {

    @Test override fun close() = startAlarmPreferenceTest {
        openSignalDialog { softClose() }
        assert()
        openSignalDialog { cancel() }
        assert()
    }

    @Test fun dialogWork() {
        val value = getRandomSignalCheck()
        val initValue = switchValue(value)

        assertFalse(initValue.contentEquals(value))
        assertEquals(initValue.size, value.size)

        startAlarmPreferenceTest {
            openSignalDialog {
                click(value)
                click(initValue)
                click(value)
                apply()
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
            initValue = getRandomSignalCheck()
            preferencesRepo.signalTypeCheck = initValue
        } while (initValue.contentEquals(value))

        return initValue
    }
}