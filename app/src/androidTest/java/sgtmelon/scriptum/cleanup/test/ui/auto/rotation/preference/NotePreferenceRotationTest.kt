package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.note.INotePreferenceTest
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.test.common.getDifferentValues

/**
 * Test of [NotePreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceRotationTest : ParentUiRotationTest(), INotePreferenceTest {

    @Test fun content() = runTest({
        preferencesRepo.sort = Sort.values().random()
        preferencesRepo.defaultColor = Color.values().random()
        preferences.isPauseSaveOn = Random.nextBoolean()
        preferences.isAutoSaveOn = Random.nextBoolean()
        preferencesRepo.savePeriod = SavePeriod.values().random()
    }) {
        rotate.toSide()
        assert()
    }

    @Test fun sortDialog() {
        val (initValue, value) = Sort.values().getDifferentValues()

        runTest({ preferencesRepo.sort = initValue }) {
            openSortDialog {
                click(value)
                rotate.toSide()
                assert()
                apply()
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
                rotate.toSide()
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
                click(value)
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.savePeriod)

    }
}