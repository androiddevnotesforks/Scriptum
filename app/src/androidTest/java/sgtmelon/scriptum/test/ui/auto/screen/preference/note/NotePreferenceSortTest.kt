package sgtmelon.scriptum.test.ui.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.ISortTest
import sgtmelon.scriptum.ui.dialog.preference.SortDialogUi

/**
 * Test for [NotePreferenceFragment] and [SortDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceSortTest : ParentUiTest(),
    INotePreferenceTest,
    ISortTest {

    @Test fun dialogClose() = runTest {
        openSortDialog { onClickCancel() }
        assert()
        openSortDialog { onCloseSoft() }
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
                onClickItem(value).onClickItem(initValue).onClickItem(value).onClickApply()
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