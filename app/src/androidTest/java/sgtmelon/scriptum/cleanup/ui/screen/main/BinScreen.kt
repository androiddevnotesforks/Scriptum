package sgtmelon.scriptum.cleanup.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.testData.SimpleInfoPage
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.cleanup.ui.dialog.ClearDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.cleanup.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.cleanup.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.longClick
import sgtmelon.test.cappuccino.utils.withMenuDrawable
import sgtmelon.test.cappuccino.utils.withMenuTitle

/**
 * Class for UI control of [BinFragment].
 */
class BinScreen : ParentRecyclerScreen(R.id.recycler_view) {

    //region Views

    private val parentContainer = getViewById(R.id.parent_container)

    private val toolbar = SimpleToolbar(R.string.title_bin, withBack = false)
    private val clearMenuItem = getViewById(R.id.item_clear)

    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.BIN)

    private fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    //endregion

    fun clearDialog(func: ClearDialogUi.() -> Unit = {}) = apply {
        clearMenuItem.click()
        ClearDialogUi(func)
    }

    fun openNoteDialog(
        item: NoteItem,
        p: Int? = random,
        func: NoteDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.longClick()
        NoteDialogUi(func, item)
    }

    fun openTextNote(
        item: NoteItem.Text,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        TextNoteScreen(func, NoteState.BIN, item, isRankEmpty)
    }

    fun openRollNote(
        item: NoteItem.Roll,
        p: Int? = random,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) = apply {
        if (p == null) return@apply

        getItem(p).view.click()
        RollNoteScreen(func, NoteState.BIN, item, isRankEmpty)
    }


    fun onAssertItem(item: NoteItem, p: Int? = random) {
        if (p == null) return

        getItem(p).assert(item)
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        if (!isEmpty) {
            toolbar.contentContainer
                .withMenuDrawable(R.id.item_clear, R.drawable.ic_clear, R.attr.clContent)
                .withMenuTitle(R.id.item_clear, R.string.menu_clear_bin)

            clearMenuItem.isDisplayed()
        }

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    companion object {
        inline operator fun invoke(func: BinScreen.() -> Unit, isEmpty: Boolean): BinScreen {
            return BinScreen().assert(isEmpty).apply(func)
        }
    }
}