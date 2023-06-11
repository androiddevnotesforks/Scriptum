package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference

/**
 * Test for [AlarmPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceTest : ParentUiTest() {

    @Test fun close() = launchMenuPreference {
        openAlarm { clickClose() }
        assert()
    }

    @Test fun signalMelody() = startSignalTest(isMelody = true, isVibration = false)

    @Test fun signalVibration() = startSignalTest(isMelody = false, isVibration = true)

    @Test fun signalBoth() = startSignalTest(isMelody = true, isVibration = true)

    private fun startSignalTest(isMelody: Boolean, isVibration: Boolean) {
        val typeCheck = booleanArrayOf(isMelody, isVibration)
        launchAlarmPreference({ preferencesRepo.signalTypeCheck = typeCheck }) { assert() }
    }

    @Test fun volumeIncreaseWork() {
        val value = Random.nextBoolean()

        launchAlarmPreference({
            preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
            preferences.isVolumeIncrease = value
        }) { onVolumeIncreaseClick() }

        assertEquals(!value, preferencesRepo.isVolumeIncrease)
    }
}