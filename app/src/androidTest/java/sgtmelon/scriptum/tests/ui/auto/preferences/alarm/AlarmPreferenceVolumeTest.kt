package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.dialog.DialogWorkCase
import sgtmelon.scriptum.source.ui.screen.dialogs.preference.VolumeDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.test.common.getDifferentValues
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceFragment] and [VolumeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceVolumeTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogWorkCase,
    DialogRotateCase {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test override fun close() = launchAlarmPreference {
        openVolumeDialog { softClose() }
        assert()
        openVolumeDialog { cancel() }
        assert()
    }

    @Test override fun work() = runWorkTest { seekTo(it) }

    @Test override fun rotateClose() = launchAlarmPreference {
        assertRotationClose { softClose() }
        assertRotationClose { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun AlarmPreferenceScreen.assertRotationClose(closeDialog: VolumeDialogUi.() -> Unit) {
        openVolumeDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() = runWorkTest { assertRotationClick(it) }

    /** Allow to click different [value] and rotate+check after that. */
    private fun VolumeDialogUi.assertRotationClick(value: Int) {
        seekTo(value)
        rotate.switch()
        assert()
    }

    /** Allow to run work test with different [action]. */
    private fun runWorkTest(action: VolumeDialogUi.(value: Int) -> Unit) {
        val (setValue, initValue) = VolumeDialogUi.VALUES.getDifferentValues()

        launchAlarmPreference({ preferencesRepo.volumePercent = initValue }) {
            openVolumeDialog {
                action(setValue)
                action(initValue)
                action(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.volumePercent)
    }
}