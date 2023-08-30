package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.value.SavePeriodCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.SavePeriodDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.notes.NotesPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [NotesPreferenceFragment] and [SavePeriodDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceSavePeriodTest : ParentUiRotationTest(),
    DialogCloseCase,
    SavePeriodCase,
    DialogRotateCase {

    override fun setUp() {
        super.setUp()

        /** Make option available for selection. */
        preferences.isAutoSaveOn = true
    }

    @Test override fun close() = launchNotesPreference {
        openSavePeriodDialog { cancel() }
        assert()
        openSavePeriodDialog { softClose() }
        assert()
    }

    @Test override fun savePeriodMin1() = super.savePeriodMin1()

    @Test override fun savePeriodMin3() = super.savePeriodMin3()

    @Test override fun savePeriodMin7() = super.savePeriodMin7()

    override fun startText(value: SavePeriod) = runWorkTest(value) { click(it) }

    @Test override fun rotateClose() = launchNotesPreference {
        assertRotationClose { softClose() }
        assertRotationClose { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun NotesPreferenceScreen.assertRotationClose(
        closeDialog: SavePeriodDialogUi.() -> Unit
    ) {
        openSavePeriodDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() = runWorkTest { assertRotationClick(it) }

    /** Allow to click different [value] and rotate+check after that. */
    private fun SavePeriodDialogUi.assertRotationClick(value: SavePeriod) {
        click(value)
        rotate.switch()
        assert()
    }

    /** Allow to run work test with different [action]. */
    private fun runWorkTest(
        value: SavePeriod? = null,
        action: SavePeriodDialogUi.(value: SavePeriod) -> Unit
    ) {
        val (setValue, initValue) = SavePeriod.values().getDifferentValues(value)
        preferencesRepo.savePeriod = initValue

        launchNotesPreference {
            openSavePeriodDialog {
                action(setValue)
                action(initValue)
                action(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.savePeriod)
    }
}