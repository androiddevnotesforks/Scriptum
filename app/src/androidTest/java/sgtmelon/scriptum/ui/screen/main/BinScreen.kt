package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.ClearDialogUi
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
import sgtmelon.scriptum.ui.item.NoteItem
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [BinFragment]
 */
class BinScreen : ParentRecyclerScreen(R.id.bin_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.bin_parent_container)

    private val toolbar = getToolbar(R.string.title_bin)
    private val clearMenuItem = getViewById(R.id.item_clear)

    private val infoContainer = InfoContainer(InfoPage.BIN)

    private fun getItem(p: Int) = NoteItem(recyclerView, p)

    //endregion

    fun openClearDialog(func: ClearDialogUi.() -> Unit = {}) = apply {
        clearMenuItem.click()
        ClearDialogUi.invoke(func)
    }

    fun openNoteDialog(noteModel: NoteModel, p: Int = random,
                       func: NoteDialogUi.() -> Unit = {}) {
        getItem(p).view.longClick()
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = random,
                     func: TextNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        TextNoteScreen.invoke(func, State.BIN, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = random,
                     func: RollNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        RollNoteScreen.invoke(func, State.BIN, noteModel)
    }


    fun onAssertItem(p: Int, noteModel: NoteModel) {
        getItem(p).assert(noteModel)
    }

    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        if (!empty) clearMenuItem.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit, empty: Boolean) =
                BinScreen().apply {assert(empty) }.apply(func)
    }

}