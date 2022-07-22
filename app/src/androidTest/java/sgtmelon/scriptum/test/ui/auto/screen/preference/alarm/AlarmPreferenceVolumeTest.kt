package sgtmelon.scriptum.test.ui.auto.screen.preference.alarm

import androidx.annotation.IntRange
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.ui.dialog.preference.VolumeDialogUi

/**
 * Test for [AlarmPreferenceFragment] and [VolumeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceVolumeTest : ParentUiTest(), IAlarmPreferenceTest {

    @Before override fun setUp() {
        super.setUp()
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

        assertEquals(value, preferences.volume)
    }

    /**
     * Switch volume to another one.
     */
    private fun switchValue(@IntRange(from = 10, to = 100) value: Int): Int {
        var initValue: Int

        do {
            initValue = VolumeDialogUi.random()
            preferences.volume = initValue
        } while (preferences.volume == value)

        return initValue
    }
}