package sgtmelon.scriptum.test.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.test.parent.ParentRotationTest
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen
import kotlin.random.Random

/**
 * Test of [NotePreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceRotationTest : ParentRotationTest() {

    private fun runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openNote(func) } } }
        }
    }

    @Test fun content() = runTest({
        preferenceRepo.sort = Sort.list.random()
        preferenceRepo.defaultColor = Color.list.random()
        preferenceRepo.pauseSaveOn = Random.nextBoolean()
        preferenceRepo.autoSaveOn = Random.nextBoolean()
        preferenceRepo.savePeriod = SavePeriod.list.random()
    }) {
        automator.rotateSide()
        assert()
    }

    @Test fun sortDialog() {
        val initValue = Sort.list.random()

        runTest({ preferenceRepo.sort = initValue }) {
            openSortDialog {
                onClickItem(getSortClick(initValue))
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }
    }

    @Sort private fun getSortClick(@Sort initValue: Int): Int {
        val newValue = Sort.list.random()
        return if (newValue == initValue) getSortClick(initValue) else newValue
    }

    @Test fun colorDialog() {
        val initValue = Color.list.random()

        runTest({ preferenceRepo.defaultColor = initValue }) {
            openColorDialog(initValue) {
                onClickItem(getColorClick(initValue))
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }
    }

    @Color private fun getColorClick(@Color initValue: Int): Int {
        val newValue = Color.list.random()
        return if (newValue == initValue) getColorClick(initValue) else newValue
    }

    @Test fun savePeriodDialog() {
        val initValue = SavePeriod.list.random()

        runTest({
            preferenceRepo.autoSaveOn = true
            preferenceRepo.savePeriod = initValue
        }) {
            openSavePeriodDialog {
                onClickItem(getSavePeriodClick(initValue))
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }
    }

    @SavePeriod private fun getSavePeriodClick(@SavePeriod initValue: Int): Int {
        val newValue = SavePeriod.list.random()
        return if (newValue == initValue) getSavePeriodClick(initValue) else newValue
    }
}