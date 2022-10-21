package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test for [NotePreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceTest : ParentUiTest(), INotePreferenceTest {

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertAll() = runTest({
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

        runTest({ preferences.isPauseSaveOn = value }) { onPauseSaveClick() }

        assertEquals(!value, preferences.isPauseSaveOn)
    }

    @Test fun autoSaveWork() {
        val value = Random.nextBoolean()

        runTest({ preferences.isAutoSaveOn = value }) { onAutoSaveClick() }

        assertEquals(!value, preferences.isAutoSaveOn)
    }
}