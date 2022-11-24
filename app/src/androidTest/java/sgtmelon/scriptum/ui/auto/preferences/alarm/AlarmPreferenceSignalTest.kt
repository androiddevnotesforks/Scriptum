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
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.ui.cases.dialog.DialogWorkCase


/**
 * Test for [AlarmPreferenceFragment] and [SignalDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceSignalTest : ParentUiTest(),
    DialogCloseCase,
    DialogWorkCase {

    @Test override fun close() = startAlarmPreferenceTest {
        openSignalDialog { softClose() }
        assert()
        openSignalDialog { cancel() }
        assert()
    }

    @Test override fun work() {
        val initValue = getRandomSignalCheck()
        val setValue = getRandomSignalCheck(initValue)

        assertFalse(initValue.contentEquals(setValue))
        assertEquals(initValue.size, setValue.size)

        startAlarmPreferenceTest({ preferencesRepo.signalTypeCheck = initValue }) {
            openSignalDialog {
                click(setValue)
                click(initValue)
                click(setValue)
                apply()
            }
            assert()
        }

        assertTrue(preferencesRepo.signalTypeCheck.contentEquals(setValue))
    }
}