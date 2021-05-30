package sgtmelon.scriptum.test.auto.screen.preference.alarm

import androidx.annotation.IntRange
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.VolumeDialogUi
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceFragment] and [VolumeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceVolumeTest : ParentUiTest(), IAlarmPreferenceTest {

    override fun setup() {
        super.setup()
        getLogic().alarmInteractor.updateSignal(booleanArrayOf(true, Random.nextBoolean()))
    }

    @Test fun dialogClose() = runTest {
        openVolumeDialog { onCloseSoft() }
        assert()
        openVolumeDialog { onClickCancel() }
        assert()
    }

    @Test fun dialogWork() {
        val value = VolumeDialogUi.random()
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openVolumeDialog { seekTo(value).seekTo(initValue).seekTo(value).onClickApply() }
            assert()
        }

        assertEquals(value, preferenceRepo.volume)
    }

    /**
     * Switch volume to another one.
     */
    private fun switchValue(@IntRange(from = 10, to = 100) value: Int): Int {
        var initValue: Int

        do {
            initValue = VolumeDialogUi.random()
            preferenceRepo.volume = initValue
        } while (preferenceRepo.volume == value)

        return initValue
    }
}