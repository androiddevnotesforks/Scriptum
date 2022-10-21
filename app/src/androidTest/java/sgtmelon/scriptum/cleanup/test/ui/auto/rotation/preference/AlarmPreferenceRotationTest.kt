package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.basic.extension.getDifferentValues
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.alarm.IAlarmPreferenceTest
import sgtmelon.scriptum.cleanup.ui.dialog.preference.VolumeDialogUi
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ParentUiRotationTest

/**
 * Test of [AlarmPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class AlarmPreferenceRotationTest : ParentUiRotationTest(), IAlarmPreferenceTest {

    @Test fun content() = runTest({
        preferencesRepo.repeat = Repeat.values().random()

        val signalArray = getLogic().getRandomSignal()
        getLogic().preferencesRepo.signalTypeCheck = signalArray

        val melodyList = runBlocking { getLogic().getMelodyList() }
        preferences.melodyUri = melodyList.random().uri

        preferences.volumePercent = VolumeDialogUi.list.random()
        preferences.isVolumeIncrease = Random.nextBoolean()
    }) {
        rotate.toSide()
        assert()
    }

    @Test fun repeatDialog() {
        val (initValue, value) = Repeat.values().getDifferentValues()

        runTest({ preferencesRepo.repeat = initValue }) {
            openRepeatDialog {
                onClickItem(value)
                rotate.toSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.repeat)
    }

    @Test fun signalDialog() {
        val initValue = getLogic().getRandomSignal()
        val value = getSignalClick(initValue)

        assertFalse(initValue.contentEquals(value))
        assertEquals(initValue.size, value.size)

        runTest({ getLogic().preferencesRepo.signalTypeCheck = initValue }) {
            openSignalDialog {
                onClickItem(value)
                rotate.toSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertTrue(getLogic().preferencesRepo.signalTypeCheck.contentEquals(value))
    }

    private fun getSignalClick(initArray: BooleanArray): BooleanArray {
        val newArray = getLogic().getRandomSignal()
        return if (initArray.contentEquals(newArray)) getSignalClick(initArray) else newArray
    }

    @Test fun melodyDialog() {
        val list = runBlocking { getLogic().getMelodyList() }
        val initValue = list.random()
        val value = getMelodyClick(list, initValue)

        assertNotEquals(initValue, value)

        runTest({
            getLogic().preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
            preferences.melodyUri = initValue.uri
        }) {
            openMelodyDialog {
                onClickItem(list.indexOf(value))
                rotate.toSide()
            }
            assert()
        }

        assertEquals(initValue.uri, preferences.melodyUri)
    }

    // TODO use getRandomValue
    private fun getMelodyClick(list: List<MelodyItem>, initValue: MelodyItem): MelodyItem {
        val newValue = list.random()
        return if (newValue == initValue) getMelodyClick(list, initValue) else newValue
    }

    @Test fun volumeDialog() {
        val (initValue, value) = VolumeDialogUi.list.toList().getDifferentValues()

        runTest({
            getLogic().preferencesRepo.signalTypeCheck = booleanArrayOf(true, Random.nextBoolean())
            preferences.volumePercent = initValue
        }) {
            openVolumeDialog {
                seekTo(value)
                rotate.toSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferences.volumePercent)
    }
}