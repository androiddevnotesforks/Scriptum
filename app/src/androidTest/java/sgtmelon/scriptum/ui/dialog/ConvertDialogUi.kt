package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.click
import sgtmelon.scriptum.ui.basic.isDisplayed
import sgtmelon.scriptum.ui.basic.isEnabled

/**
 * Class for UI control of [MessageDialog] which open from [NoteActivity] on convert
 */
class ConvertDialogUi(private val noteModel: NoteModel) : ParentDialogUi() {

    // TODO add callback for getting result in [NotePanel]

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_convert)
    private val messageText = getViewByText(when (noteModel.noteEntity.type) {
        NoteType.TEXT -> R.string.dialog_text_convert_to_roll
        NoteType.ROLL -> R.string.dialog_roll_convert_to_text
    })

    private val noButton = getViewByText(R.string.dialog_button_no)
    private val yesButton = getViewByText(R.string.dialog_button_yes)

    //endregion

    fun onClickNo() = waitClose { noButton.click() }

    fun onClickYes() = waitClose { yesButton.click() }


    fun assert() {
        titleText.isDisplayed()
        messageText.isDisplayed()

        noButton.isDisplayed().isEnabled(enabled = true)
        yesButton.isDisplayed().isEnabled(enabled = true)
    }

    companion object {
        operator fun invoke(func: ConvertDialogUi.() -> Unit, noteModel: NoteModel) =
                ConvertDialogUi(noteModel).apply { waitOpen { assert() } }.apply(func)
    }

}