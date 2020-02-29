package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.ClearDialogUi
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.item.NoteItemUi
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [BinFragment].
 */
class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.bin_parent_container)

    private val toolbar = SimpleToolbar(R.string.title_bin)
    private val clearMenuItem = getViewById(R.id.item_clear)

    private val infoContainer = InfoContainer(InfoPage.BIN)

    private fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    //endregion

    fun clearDialog(func: ClearDialogUi.() -> Unit = {}) = apply {
        clearMenuItem.click()
        ClearDialogUi(func)
    }

    fun openNoteDialog(noteItem: NoteItem, p: Int = random,
                       func: NoteDialogUi.() -> Unit = {}) = apply {
        getItem(p).view.longClick()
        NoteDialogUi(func, noteItem)
    }

    fun openTextNote(noteItem: NoteItem, p: Int = random, isRankEmpty: Boolean = true,
                     func: TextNoteScreen.() -> Unit = {}) = apply {
        getItem(p).view.click()
        TextNoteScreen(func, State.BIN, noteItem, isRankEmpty)
    }

    fun openRollNote(noteItem: NoteItem, p: Int = random, isRankEmpty: Boolean = true,
                     func: RollNoteScreen.() -> Unit = {}) = apply {
        getItem(p).view.click()
        RollNoteScreen(func, State.BIN, noteItem, isRankEmpty)
    }


    fun onAssertItem(noteItem: NoteItem, p: Int = random) {
        getItem(p).assert(noteItem)
    }

    fun assert(empty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        if (!empty) clearMenuItem.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit, empty: Boolean): BinScreen {
            return BinScreen().assert(empty).apply(func)
        }
    }

}