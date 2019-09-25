package sgtmelon.scriptum.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.basic.extension.withDrawable
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
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

    private val infoContainer = getViewById(R.id.bin_info_include)
    private val infoImage = getViewById(R.id.info_image).includeParent(infoContainer)
    private val infoTitleText = getView(R.id.info_title_text, R.string.info_bin_empty_title)
    private val infoDetailsText = getView(R.id.info_details_text, R.string.info_bin_empty_details)

    //endregion

    fun openClearDialog(func: ClearDialogUi.() -> Unit = {}) {
        clearMenuItem.click()
        ClearDialogUi.invoke(func)
    }

    fun openNoteDialog(noteModel: NoteModel, p: Int = random,
                       func: NoteDialogUi.() -> Unit = {}) {
        recyclerView.longClick(p)
        NoteDialogUi.invoke(func, noteModel)
    }

    fun openTextNote(noteModel: NoteModel, p: Int = random,
                     func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.BIN, noteModel)
    }

    fun openRollNote(noteModel: NoteModel, p: Int = random,
                     func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.BIN, noteModel)
    }


    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        if (!empty) clearMenuItem.isDisplayed()

        infoContainer.isDisplayed(empty)
        infoImage.isDisplayed(empty).withDrawable(R.mipmap.img_info_bin, R.attr.clContent)
        infoTitleText.isDisplayed(empty)
        infoDetailsText.isDisplayed(empty)

        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: BinScreen.() -> Unit, empty: Boolean) =
                BinScreen().apply {assert(empty) }.apply(func)
    }

}