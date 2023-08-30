package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.value.SortCase
import sgtmelon.scriptum.source.ui.screen.dialogs.select.SortDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.notes.NotesPreferenceScreen
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [NotesPreferenceFragment] and [SortDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceSortTest : ParentUiRotationTest(),
    DialogCloseCase,
    SortCase,
    DialogRotateCase {

    @Test override fun close() = launchNotesPreference {
        openSortDialog { cancel() }
        assert()
        openSortDialog { softClose() }
        assert()
    }

    @Test override fun sortChange() = super.sortChange()

    @Test override fun sortCreate() = super.sortCreate()

    @Test override fun sortRank() = super.sortRank()

    @Test override fun sortColor() = super.sortColor()

    override fun startTest(value: Sort) = runWorkTest(value) { click(it) }

    @Test override fun rotateClose() = launchNotesPreference {
        assertRotationClose { softClose() }
        assertRotationClose { cancel() }
    }

    /** Allow to [closeDialog] in different ways. */
    private fun NotesPreferenceScreen.assertRotationClose(closeDialog: SortDialogUi.() -> Unit) {
        openSortDialog {
            rotate.switch()
            assert()
            closeDialog(this)
        }
        assert()
    }

    @Test override fun rotateWork() = runWorkTest { assertRotationClick(it) }

    /** Allow to click different [value] and rotate+check after that. */
    private fun SortDialogUi.assertRotationClick(value: Sort) {
        click(value)
        rotate.switch()
        assert()
    }

    /** Allow to run work test with different [action]. */
    private fun runWorkTest(value: Sort? = null, action: SortDialogUi.(value: Sort) -> Unit) {
        val (setValue, initValue) = Sort.values().getDifferentValues(value)
        preferencesRepo.sort = initValue

        launchNotesPreference {
            openSortDialog {
                action(setValue)
                action(initValue)
                action(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.sort)
    }
}