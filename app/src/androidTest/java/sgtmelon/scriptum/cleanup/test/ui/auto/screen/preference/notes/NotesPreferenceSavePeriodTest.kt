package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SavePeriodDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.ui.cases.value.SavePeriodCase

/**
 * Test for [NotesPreferenceFragment] and [SavePeriodDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceSavePeriodTest : ParentUiTest(),
    INotesPreferenceTest,
    DialogCloseCase,
    SavePeriodCase {

    @Test override fun close() = runTest({ preferences.isAutoSaveOn = true }) {
        openSavePeriodDialog { cancel() }
        assert()
        openSavePeriodDialog { softClose() }
        assert()
    }

    @Test override fun savePeriodMin1() = super.savePeriodMin1()

    @Test override fun savePeriodMin3() = super.savePeriodMin3()

    @Test override fun savePeriodMin7() = super.savePeriodMin7()

    override fun startText(value: SavePeriod) {
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest({ preferences.isAutoSaveOn = true }) {
            openSavePeriodDialog {
                click(value)
                apply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.savePeriod)
    }

    /**
     * Switch [SavePeriod] to another one. Setup savePeriod for application which not
     * equals [value].
     */
    private fun switchValue(value: SavePeriod): SavePeriod {
        val list = SavePeriod.values()
        var initValue: SavePeriod

        do {
            initValue = list.random()
            preferencesRepo.savePeriod = initValue
        } while (initValue == value)

        return initValue
    }
}