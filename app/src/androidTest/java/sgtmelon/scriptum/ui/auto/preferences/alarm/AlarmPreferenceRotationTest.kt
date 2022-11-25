package sgtmelon.scriptum.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.ui.dialog.preference.VolumeDialogUi
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.parent.utils.getRandomSignalCheck
import sgtmelon.test.common.getDifferentValues

/**
 * Test of [AlarmPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRotationTest : ParentUiRotationTest() {

    @Test fun content() = startAlarmPreferenceTest({
        preferencesRepo.repeat = Repeat.values().random()
        preferencesRepo.signalTypeCheck = getRandomSignalCheck()

        // TODO inject getMelodyUseCase
        val melodyList = runBlocking { AlarmPreferenceLogic().getMelodyList() }
        preferences.melodyUri = melodyList.random().uri

        preferencesRepo.volumePercent = VolumeDialogUi.list.random()
        preferences.isVolumeIncrease = Random.nextBoolean()
    }) {
        rotate.toSide()
        assert()
    }

    @Test fun signalDialog() {
        val initValue = getRandomSignalCheck()
        val setValue = getRandomSignalCheck(initValue)

        startAlarmPreferenceTest({ preferencesRepo.signalTypeCheck = initValue }) {
            openSignalDialog {
                click(setValue)
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertTrue(preferencesRepo.signalTypeCheck.contentEquals(setValue))
    }

    @Test fun repeatDialog() {
        val (setValue, initValue) = Repeat.values().getDifferentValues()

        startAlarmPreferenceTest({ preferencesRepo.repeat = initValue }) {
            openRepeatDialog {
                click(setValue)
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.repeat)
    }

    @Test fun melodyDialog() {
        // TODO inject getMelodyUseCase
        val list = runBlocking { AlarmPreferenceLogic().getMelodyList() }
        val (setValue, initValue) = list.getDifferentValues()

        startAlarmPreferenceTest({
            preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
            preferences.melodyUri = initValue.uri
        }) {
            openMelodyDialog {
                click(list.indexOf(setValue))
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue.uri, preferences.melodyUri)
    }

    @Test fun volumeDialog() {
        val (setValue, initValue) = VolumeDialogUi.list.getDifferentValues()

        startAlarmPreferenceTest({
            preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
            preferencesRepo.volumePercent = initValue
        }) {
            openVolumeDialog {
                seekTo(setValue)
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.volumePercent)
    }
}