package sgtmelon.scriptum.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.getDifferentValues
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.test.parent.ParentRotationTest
import sgtmelon.scriptum.test.ui.auto.screen.preference.note.INotePreferenceTest

/**
 * Test of [NotePreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceRotationTest : ParentRotationTest(), INotePreferenceTest {

    @Test fun content() = runTest({
        preferencesRepo.sort = Sort.values().random()
        preferencesRepo.defaultColor = Color.values().random()
        preferences.isPauseSaveOn = Random.nextBoolean()
        preferences.isAutoSaveOn = Random.nextBoolean()
        preferencesRepo.savePeriod = SavePeriod.values().random()
    }) {
        automator.rotateSide()
        assert()
    }

    @Test fun sortDialog() {
        val (initValue, value) = Sort.values().getDifferentValues()

        runTest({ preferencesRepo.sort = initValue }) {
            openSortDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.sort)
    }

    @Test fun colorDialog() {
        val (initValue, value) = Color.values().getDifferentValues()

        runTest({ preferencesRepo.defaultColor = initValue }) {
            openColorDialog(initValue) {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.defaultColor)
    }

    @Test fun savePeriodDialog() {
        val (initValue, value) = SavePeriod.values().getDifferentValues()

        runTest({
            preferences.isAutoSaveOn = true
            preferencesRepo.savePeriod = initValue
        }) {
            openSavePeriodDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.savePeriod)

    }
}