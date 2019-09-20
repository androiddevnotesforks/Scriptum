package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.click
import sgtmelon.scriptum.ui.basic.isDisplayed
import sgtmelon.scriptum.ui.basic.longClick
import sgtmelon.scriptum.ui.dialog.ClearDialogUi
import sgtmelon.scriptum.ui.dialog.NoteDialogUi
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

    private val infoTitleText = getViewById(R.id.info_title_text).withText(R.string.info_bin_empty_title)
    private val infoDetailsText = getViewById(R.id.info_details_text).withText(R.string.info_bin_empty_details)

    //endregion

    fun openClearDialog(func: ClearDialogUi.() -> Unit = {}) {
        clearMenuItem.click()
        ClearDialogUi.invoke(func)
    }

    fun openNoteDialog(noteModel: NoteModel, p: Int = positionRandom,
                       func: NoteDialogUi.() -> Unit = {}) {
        recyclerView.longClick(p)
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = positionRandom,
                     func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.BIN, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = positionRandom,
                     func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.BIN, noteModel)
    }


    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        if (!empty) clearMenuItem.isDisplayed()

        infoTitleText.isDisplayed(empty)
        infoDetailsText.isDisplayed(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit, empty: Boolean) =
                BinScreen().apply {assert(empty) }.apply(func)
    }

}