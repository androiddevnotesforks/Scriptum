package sgtmelon.scriptum.tests.ui.auto.preferences.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.ui.screen.dialogs.select.SortDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchNotesPreference
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.value.SortCase
import sgtmelon.test.common.getDifferentValues

/**
 * Test for [NotesPreferenceFragment] and [SortDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceSortTest : ParentUiTest(),
    DialogCloseCase,
    SortCase {

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

    override fun startTest(value: Sort) {
        val (setValue, initValue) = Sort.values().getDifferentValues(value)
        preferencesRepo.sort = initValue

        launchNotesPreference {
            openSortDialog {
                click(setValue)
                click(initValue)
                click(setValue)
                apply()
            }
            assert()
        }

        assertEquals(setValue, preferencesRepo.sort)
    }
}