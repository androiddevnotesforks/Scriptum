package sgtmelon.scriptum.cleanup.test.ui.auto.screen.preference.notes

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SortDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.ui.cases.value.SortCase

/**
 * Test for [NotesPreferenceFragment] and [SortDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotesPreferenceSortTest : ParentUiTest(),
    INotesPreferenceTest,
    SortCase {

    @Test fun dialogClose() = runTest {
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
        val initValue = switchValue(value)

        assertNotEquals(initValue, value)

        runTest {
            openSortDialog {
                click(value)
                click(initValue)
                click(value)
                apply()
            }
            assert()
        }

        assertEquals(value, preferencesRepo.sort)
    }

    /**
     * Switch [Sort] to another one. Setup sort for application which not equals [value].
     */
    private fun switchValue(value: Sort): Sort {
        val list = Sort.values()
        var initValue: Sort

        do {
            initValue = list.random()
            preferencesRepo.sort = initValue
        } while (initValue == value)

        return initValue
    }
}