package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.dialog.DialogWorkCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.SignalDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.scriptum.source.utils.getRandomSignalCheck


/**
 * Test for [AlarmPreferenceFragment] and [SignalDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceSignalTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogWorkCase,
    DialogRotateCase {

    @Test override fun close() = launchAlarmPreference {
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

        launchAlarmPreference({ preferencesRepo.signalTypeCheck = initValue }) {
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

    @Test override fun rotateClose() = launchAlarmPreference {
        assertRotationClose { softClose() }
        assert()
        assertRotationClose { cancel() }
        assert()
    }

    /** Allow to [closeDialog] in different ways. */
    private fun AlarmPreferenceScreen.assertRotationClose(closeDialog: SignalDialogUi.() -> Unit) {
        openSignalDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() {
        val initValue = getRandomSignalCheck()
        val setValue = getRandomSignalCheck(initValue)

        assertFalse(initValue.contentEquals(setValue))
        assertEquals(initValue.size, setValue.size)

        launchAlarmPreference({ preferencesRepo.signalTypeCheck = initValue }) {
            openSignalDialog {
                assertRotationClick(setValue)
                assertRotationClick(initValue)
                assertRotationClick(setValue)
                apply()
            }
            assert()
        }

        assertTrue(preferencesRepo.signalTypeCheck.contentEquals(setValue))
    }

    /** Allow to click different [value] and rotate+check after that. */
    private fun SignalDialogUi.assertRotationClick(value: BooleanArray) {
        click(value)
        rotate.switch()
        assert()
    }
}