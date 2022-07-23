package sgtmelon.scriptum.test.ui.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test for [NotePreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceTest : ParentUiTest(), INotePreferenceTest {

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertAll() = runTest({
        preferences.sort = Sort.list.random()
        preferences.defaultColor = Color.list.random()
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