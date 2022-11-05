package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest

/**
 * Test for [AlarmPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceTest : ParentUiTest(), IAlarmPreferenceTest {

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertMelody() = startAssertTest(isMelody = true, isVibration = false)

    @Test fun assertVibration() = startAssertTest(isMelody = false, isVibration = true)

    @Test fun assertBoth() = startAssertTest(isMelody = true, isVibration = true)

    private fun startAssertTest(isMelody: Boolean, isVibration: Boolean) = runTest({
        getLogic().preferencesRepo.signalTypeCheck = booleanArrayOf(isMelody, isVibration)
    }) { assert() }

    @Test fun volumeIncreaseWork() {
        val value = Random.nextBoolean()

        runTest({
            getLogic().preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
            preferences.isVolumeIncrease = value
        }) { onVolumeIncreaseClick() }

        assertEquals(!value, preferences.isVolumeIncrease)
    }
}