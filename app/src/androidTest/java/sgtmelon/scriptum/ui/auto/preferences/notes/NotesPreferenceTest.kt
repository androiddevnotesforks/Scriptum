package sgtmelon.scriptum.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchMenuPreference
import sgtmelon.scriptum.parent.ui.tests.launchNotesPreference

/**
 * Test for [NotesPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceTest : ParentUiTest() {

    @Test fun close() = launchMenuPreference {
        openNotes { clickClose() }
        assert()
    }

    @Test fun assertAll() = launchNotesPreference({
        preferencesRepo.sort = Sort.values().random()
        preferencesRepo.defaultColor = Color.values().random()
        preferences.isPauseSaveOn = Random.nextBoolean()
        preferences.isAutoSaveOn = Random.nextBoolean()
        preferencesRepo.savePeriod = SavePeriod.values().random()
    }) {
        assert()
    }

    @Test fun pauseSaveWork() {
        val value = Random.nextBoolean()

        launchNotesPreference({ preferences.isPauseSaveOn = value }) { switchPauseSave() }

        assertEquals(!value, preferences.isPauseSaveOn)
    }

    @Test fun autoSaveWork() {
        val value = Random.nextBoolean()

        launchNotesPreference({ preferences.isAutoSaveOn = value }) { switchAutoSave() }

        assertEquals(!value, preferences.isAutoSaveOn)
    }
}