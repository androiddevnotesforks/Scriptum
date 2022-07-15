package sgtmelon.scriptum.test.ui.auto.rotation.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.test.ui.auto.screen.preference.note.INotePreferenceTest
import sgtmelon.scriptum.test.parent.ParentRotationTest
import kotlin.random.Random

/**
 * Test of [NotePreferenceFragment] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceRotationTest : ParentRotationTest(), INotePreferenceTest {

    @Test fun content() = runTest({
        preferences.sort = Sort.list.random()
        preferences.defaultColor = Color.list.random()
        preferences.pauseSaveOn = Random.nextBoolean()
        preferences.autoSaveOn = Random.nextBoolean()
        preferences.savePeriod = SavePeriod.list.random()
    }) {
        automator.rotateSide()
        assert()
    }

    @Test fun sortDialog() {
        val initValue = Sort.list.random()
        val value = getSortClick(initValue)

        assertNotEquals(initValue, value)

        runTest({ preferences.sort = initValue }) {
            openSortDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferences.sort)
    }

    @Sort private fun getSortClick(@Sort initValue: Int): Int {
        val newValue = Sort.list.random()
        return if (newValue == initValue) getSortClick(initValue) else newValue
    }

    @Test fun colorDialog() {
        val initValue = Color.list.random()
        val value = getColorClick(initValue)

        assertNotEquals(initValue, value)

        runTest({ preferences.defaultColor = initValue }) {
            openColorDialog(initValue) {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferences.defaultColor)
    }

    @Color private fun getColorClick(@Color initValue: Int): Int {
        val newValue = Color.list.random()
        return if (newValue == initValue) getColorClick(initValue) else newValue
    }

    @Test fun savePeriodDialog() {
        val initValue = SavePeriod.list.random()
        val value = getSavePeriodClick(initValue)

        assertNotEquals(initValue, value)

        runTest({
            preferences.autoSaveOn = true
            preferences.savePeriod = initValue
        }) {
            openSavePeriodDialog {
                onClickItem(value)
                automator.rotateSide()
                assert()
                onClickApply()
            }
            assert()
        }

        assertEquals(value, preferences.savePeriod)

    }

    @SavePeriod private fun getSavePeriodClick(@SavePeriod initValue: Int): Int {
        val newValue = SavePeriod.list.random()
        return if (newValue == initValue) getSavePeriodClick(initValue) else newValue
    }
}