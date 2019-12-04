package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.dialog.SheetAddDialog
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control [SheetAddDialog]
 */
class AddDialogUi : ParentUi(), IDialogUi {

    //region Views

    private val navigationContainer = getViewById(R.id.add_container)
    private val navigationView = getViewById(R.id.add_navigation)

    private val titleText = getViewByText(R.string.dialog_title_add_note)
    private val textButton = getViewByText(R.string.dialog_add_text)
    private val rollButton = getViewByText(R.string.dialog_add_roll)

    //endregion

    fun createText(noteItem: NoteItem, func: TextNoteScreen.() -> Unit = {}) {
        textButton.click()
        TextNoteScreen.invoke(func, State.NEW, noteItem)
    }

    fun createRoll(noteItem: NoteItem, func: RollNoteScreen.() -> Unit = {}) {
        rollButton.click()
        RollNoteScreen.invoke(func, State.NEW, noteItem)
    }

    fun onCloseSwipe() = waitClose { navigationView.swipeDown() }


    fun assert() {
        navigationContainer.isDisplayed().withBackground(when(theme == Theme.LIGHT) {
            true -> R.drawable.bg_dialog_light
            false -> R.drawable.bg_dialog_dark
        })

        navigationView.isDisplayed()

        titleText.isDisplayed().withTextColor(R.attr.clContentSecond)
        textButton.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
        rollButton.isDisplayed().withTextColor(R.attr.clContent).isEnabled()
    }

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit): AddDialogUi {
            return AddDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }

}