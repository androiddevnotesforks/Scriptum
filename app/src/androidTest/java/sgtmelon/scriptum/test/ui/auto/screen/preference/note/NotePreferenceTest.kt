package sgtmelon.scriptum.test.ui.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import kotlin.random.Random

/**
 * Test for [NotePreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceTest : ParentUiTest(), INotePreferenceTest {

    @Test fun close() = runTest { onClickClose() }

    @Test fun assertAll() = runTest({
        preferenceRepo.sort = Sort.list.random()
        preferenceRepo.defaultColor = Color.list.random()
        preferenceRepo.pauseSaveOn = Random.nextBoolean()
        preferenceRepo.autoSaveOn = Random.nextBoolean()
        preferenceRepo.savePeriod = SavePeriod.list.random()
    }) {
        assert()
    }

    @Test fun pauseSaveWork() {
        val value = Random.nextBoolean()

        runTest({ preferenceRepo.pauseSaveOn = value }) { onPauseSaveClick() }

        assertEquals(!value, preferenceRepo.pauseSaveOn)
    }

    @Test fun autoSaveWork() {
        val value = Random.nextBoolean()

        runTest({ preferenceRepo.autoSaveOn = value }) { onAutoSaveClick() }

        assertEquals(!value, preferenceRepo.autoSaveOn)
    }
}