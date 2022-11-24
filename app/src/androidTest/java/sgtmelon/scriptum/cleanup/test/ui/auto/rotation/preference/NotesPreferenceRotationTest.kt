package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.notes.INotesPreferenceTest
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiRotationTest
import sgtmelon.test.common.getDifferentValues

/**
 * Test of [NotesPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceRotationTest : ParentUiRotationTest(), INotesPreferenceTest {

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
        val (setValue, initValue) = Sort.values().getDifferentValues()

        runTest({ preferencesRepo.sort = initValue }) {
            openSortDialog {
                click(setValue)
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.sort)
    }

    @Test fun colorDialog() {
        val (setValue, initValue) = Color.values().getDifferentValues()

        runTest({ preferencesRepo.defaultColor = initValue }) {
            openColorDialog(initValue) {
                onClickItem(setValue)
                rotate.toSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.defaultColor)
    }

    @Test fun savePeriodDialog() {
        val (setValue, initValue) = SavePeriod.values().getDifferentValues()

        runTest({
            preferences.isAutoSaveOn = true
            preferencesRepo.savePeriod = initValue
        }) {
            openSavePeriodDialog {
                click(setValue)
                rotate.toSide()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.savePeriod)

    }
}