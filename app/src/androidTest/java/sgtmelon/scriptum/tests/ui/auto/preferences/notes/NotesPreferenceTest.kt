package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.cases.screen.CloseScreenCase
import sgtmelon.scriptum.source.cases.screen.ContentScreenCase
import sgtmelon.scriptum.source.cases.screen.RotateScreenCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import kotlin.random.Random

/**
 * Test for [NotesPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceTest : ParentUiRotationTest(),
    CloseScreenCase,
    ContentScreenCase,
    RotateScreenCase {

    @Test override fun closeScreen() = launchMenuPreference {
        openNotes { clickClose() }
        assert()
    }

    @Test override fun content() = launchNotesPreference({ setupRandomContent() }) {
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

    @Test override fun rotateScreen() = launchNotesPreference({ setupRandomContent() }) {
        rotate.switch()
        assert()
    }

    /** Setup random content for screen tests. */
    private fun setupRandomContent() {
        preferencesRepo.sort = Sort.values().random()
        preferencesRepo.defaultColor = Color.values().random()
        preferences.isPauseSaveOn = Random.nextBoolean()
        preferences.isAutoSaveOn = Random.nextBoolean()
        preferencesRepo.savePeriod = SavePeriod.values().random()
    }
}