package sgtmelon.scriptum.test.ui.auto.screen.preference.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import kotlin.random.Random

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
        getLogic().alarmInteractor.updateSignal(booleanArrayOf(isMelody, isVibration))
    }) { assert() }

    @Test fun volumeIncreaseWork() {
        val value = Random.nextBoolean()

        runTest({
            getLogic().alarmInteractor.updateSignal(booleanArrayOf(true, Random.nextBoolean()))
            preferenceRepo.volumeIncrease = value
        }) { onVolumeIncreaseClick() }

        assertEquals(!value, preferenceRepo.volumeIncrease)
    }
}