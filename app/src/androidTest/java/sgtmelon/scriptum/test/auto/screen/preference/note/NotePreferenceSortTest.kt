package sgtmelon.scriptum.test.auto.screen.preference.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.test.parent.ParentUiTest
import sgtmelon.scriptum.test.parent.situation.ISortTest
import sgtmelon.scriptum.ui.dialog.preference.SortDialogUi
import sgtmelon.scriptum.ui.screen.preference.NotePreferenceScreen

/**
 * Test for [NotePreferenceFragment] and [SortDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class NotePreferenceSortTest : ParentUiTest(), ISortTest {

    private fun runTest(before: () -> Unit = {}, func: NotePreferenceScreen.() -> Unit) {
        launch(before) {
            mainScreen { notesScreen(isEmpty = true) { openPreference { openNote(func) } } }
        }
    }

    @Test fun dialogClose() = runTest({ preferenceRepo.autoSaveOn = true }) {
        openSortDialog { onClickCancel() }
        assert()
        openSortDialog { onCloseSoft() }
        assert()
    }

    @Test override fun sortChange() = super.sortChange()

    @Test override fun sortCreate() = super.sortCreate()

    @Test override fun sortRank() = super.sortRank()

    @Test override fun sortColor() = super.sortColor()

    override fun startTest(@Sort sort: Int) = runTest({ switchValue(sort) }) {
        openSortDialog { onClickItem(sort).onClickApply() }
        assert()
    }

    /**
     * Switch [Sort] to another one.
     */
    private fun switchValue(@Sort sort: Int) {
        val list = Sort.list

        while (preferenceRepo.sort == sort) {
            preferenceRepo.sort = list.random()
        }
    }
}