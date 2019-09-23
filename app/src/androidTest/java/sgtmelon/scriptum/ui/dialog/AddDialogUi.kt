package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.swipeDown
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.dialog.SheetAddDialog
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control [SheetAddDialog]
 */
class AddDialogUi : ParentDialogUi() {

    //region Views

    private val navigationView = getViewById(R.id.add_navigation)
    private val textButton = getViewByText(R.string.dialog_add_text)
    private val rollButton = getViewByText(R.string.dialog_add_roll)

    //endregion

    fun createTextNote(noteModel: NoteModel, func: TextNoteScreen.() -> Unit = {}) {
        textButton.click()
        TextNoteScreen.invoke(func, State.NEW, noteModel)
    }

    fun createRollNote(noteModel: NoteModel, func: RollNoteScreen.() -> Unit = {}) {
        rollButton.click()
        RollNoteScreen.invoke(func, State.NEW, noteModel)
    }

    fun onCloseSwipe() = waitClose { navigationView.swipeDown() }


    fun assert() {
        navigationView.isDisplayed()

        textButton.isDisplayed().isEnabled()
        rollButton.isDisplayed().isEnabled()
    }

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit) =
                AddDialogUi().apply { waitOpen { assert() } }.apply(func)
    }

}