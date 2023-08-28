package sgtmelon.scriptum.tests.ui.auto.preferences.alarm

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.ui.screen.dialogs.preference.VolumeDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceLogic
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchAlarmPreference
import sgtmelon.scriptum.source.utils.getRandomSignalCheck
import sgtmelon.test.common.getDifferentValues
import kotlin.random.Random

/**
 * Test of [AlarmPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRotationTest : ParentUiRotationTest() {

    @Test fun content() = launchAlarmPreference({
        preferencesRepo.repeat = Repeat.values().random()
        preferencesRepo.signalTypeCheck = getRandomSignalCheck()

        // TODO inject getMelodyUseCase
        val melodyList = runBlocking { AlarmPreferenceLogic().getMelodyList() }
        preferences.melodyUri = melodyList.random().uri

        preferencesRepo.volumePercent = VolumeDialogUi.VALUES.random()
        preferences.isVolumeIncrease = Random.nextBoolean()
    }) {
        rotate.toSide()
        assert()
    }

    @Test fun signalDialog() {
        val initValue = getRandomSignalCheck()
        val setValue = getRandomSignalCheck(initValue)

        launchAlarmPreference({ preferencesRepo.signalTypeCheck = initValue }) {
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

        launchAlarmPreference({ preferencesRepo.repeat = initValue }) {
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
        TODO("Grant permission for access melodies")

        // TODO inject getMelodyUseCase
        val list = runBlocking { AlarmPreferenceLogic().getMelodyList() }
        val (setValue, initValue) = list.getDifferentValues()

        launchAlarmPreference({
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
        val (setValue, initValue) = VolumeDialogUi.VALUES.getDifferentValues()

        launchAlarmPreference({
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