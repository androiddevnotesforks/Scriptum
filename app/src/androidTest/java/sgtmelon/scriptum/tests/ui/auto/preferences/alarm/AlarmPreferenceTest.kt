package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.cases.screen.CloseScreenCase
import sgtmelon.scriptum.source.cases.screen.RotateScreenCase
import sgtmelon.scriptum.source.ui.screen.dialogs.preference.VolumeDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference
import sgtmelon.scriptum.source.utils.getRandomSignalCheck
import kotlin.random.Random

/**
 * Test for [AlarmPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceTest : ParentUiRotationTest(),
    CloseScreenCase,
    RotateScreenCase {

    @Test override fun closeScreen() = launchMenuPreference {
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

    @Test override fun rotateScreen() = launchAlarmPreference({
        preferencesRepo.repeat = Repeat.values().random()
        preferencesRepo.signalTypeCheck = getRandomSignalCheck()

        // TODO inject getMelodyUseCase
        val melodyList = runBlocking { AlarmPreferenceLogic().getMelodyList() }
        preferences.melodyUri = melodyList.random().uri

        preferencesRepo.volumePercent = VolumeDialogUi.VALUES.random()
        preferences.isVolumeIncrease = Random.nextBoolean()
    }) {
        rotate.switch()
        assert()
    }
}