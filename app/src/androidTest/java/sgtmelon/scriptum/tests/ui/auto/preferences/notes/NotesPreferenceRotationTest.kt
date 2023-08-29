package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.test.common.getDifferentValues
import kotlin.random.Random

/**
 * Test of [NotesPreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceRotationTest : ParentUiRotationTest() {

    @Test fun content() = launchNotesPreference({
        preferencesRepo.sort = Sort.values().random()
        preferencesRepo.defaultColor = Color.values().random()
        preferences.isPauseSaveOn = Random.nextBoolean()
        preferences.isAutoSaveOn = Random.nextBoolean()
        preferencesRepo.savePeriod = SavePeriod.values().random()
    }) {
        rotate.switch()
        assert()
    }

    @Test fun sortDialog() {
        val (setValue, initValue) = Sort.values().getDifferentValues()

        launchNotesPreference({ preferencesRepo.sort = initValue }) {
            openSortDialog {
                click(setValue)
                rotate.switch()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.sort)
    }

    @Test fun colorDialog() {
        val (setValue, initValue) = Color.values().getDifferentValues()

        launchNotesPreference({ preferencesRepo.defaultColor = initValue }) {
            openColorDialog(initValue) {
                select(setValue)
                rotate.switch()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.defaultColor)
    }

    @Test fun savePeriodDialog() {
        val (setValue, initValue) = SavePeriod.values().getDifferentValues()

        launchNotesPreference({
            preferences.isAutoSaveOn = true
            preferencesRepo.savePeriod = initValue
        }) {
            openSavePeriodDialog {
                click(setValue)
                rotate.switch()
                assert()
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.savePeriod)
    }
}