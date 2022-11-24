package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.annotation.IntRange
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.dialog.preference.VolumeDialogUi
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [AlarmPreferenceFragment] and [VolumeDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceVolumeTest : ParentUiTest() {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
    }

    @Test fun dialogClose() = startAlarmPreferenceTest {
        openVolumeDialog { softClose() }
        assert()
        openVolumeDialog { onClickCancel() }
        assert()
    }

    @Test fun dialogWork() {
        val value = VolumeDialogUi.list.random()
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        startAlarmPreferenceTest {
            openVolumeDialog { seekTo(value).seekTo(initValue).seekTo(value).apply() }
            assert()
        }

        assertEquals(value, preferencesRepo.volumePercent)
    }

    /**
     * Switch volume to another one.
     */
    private fun switchValue(@IntRange(from = 10, to = 100) value: Int): Int {
        var initValue: Int

        do {
            initValue = VolumeDialogUi.list.random()
            preferencesRepo.volumePercent = initValue
        } while (initValue == value)

        return initValue
    }
}