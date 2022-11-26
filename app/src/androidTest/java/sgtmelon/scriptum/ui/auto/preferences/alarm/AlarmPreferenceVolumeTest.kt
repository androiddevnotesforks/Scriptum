package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.dialog.preference.VolumeDialogUi
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchAlarmPreference
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.ui.cases.dialog.DialogWorkCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [AlarmPreferenceFragment] and [VolumeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceVolumeTest : ParentUiTest(),
    DialogCloseCase,
    DialogWorkCase {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test override fun close() = launchAlarmPreference {
        openVolumeDialog { softClose() }
        assert()
        openVolumeDialog { onClickCancel() }
        assert()
    }

    @Test override fun work() {
        val (setValue, initValue) = VolumeDialogUi.list.getDifferentValues()

        launchAlarmPreference({ preferencesRepo.volumePercent = initValue }) {
            openVolumeDialog {
                seekTo(setValue)
                seekTo(initValue)
                seekTo(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.volumePercent)
    }
}