package sgtmelon.scriptum.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.ui.screen.dialogs.select.SavePeriodDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.ui.cases.value.SavePeriodCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [NotesPreferenceFragment] and [SavePeriodDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceSavePeriodTest : ParentUiTest(),
    DialogCloseCase,
    SavePeriodCase {

    @Test override fun close() = launchNotesPreference({ preferences.isAutoSaveOn = true }) {
        openSavePeriodDialog { cancel() }
        assert()
        openSavePeriodDialog { softClose() }
        assert()
    }

    @Test override fun savePeriodMin1() = super.savePeriodMin1()

    @Test override fun savePeriodMin3() = super.savePeriodMin3()

    @Test override fun savePeriodMin7() = super.savePeriodMin7()

    override fun startText(value: SavePeriod) {
        val (setValue, initValue) = SavePeriod.values().getDifferentValues(value)
        preferencesRepo.savePeriod = initValue

        launchNotesPreference({ preferences.isAutoSaveOn = true }) {
            openSavePeriodDialog {
                click(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.savePeriod)
    }
}