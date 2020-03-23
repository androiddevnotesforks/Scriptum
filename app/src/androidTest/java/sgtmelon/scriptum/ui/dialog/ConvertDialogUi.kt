package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.presentation.screen.ui.note.NoteActivity
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control of [MessageDialog] which open from [NoteActivity] on convert.
 */
class ConvertDialogUi(noteItem: NoteItem, private val callback: Callback) : ParentUi(), IDialogUi {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_convert)
    private val messageText = getViewByText(when (noteItem.type) {
        NoteType.TEXT -> R.string.dialog_text_convert_text
        NoteType.ROLL -> R.string.dialog_roll_convert_roll
    })

    private val noButton = getViewByText(R.string.dialog_button_no)
    private val yesButton = getViewByText(R.string.dialog_button_yes)

    //endregion

    fun onClickNo() = waitClose { noButton.click() }

    fun onClickYes() = waitClose {
        yesButton.click()
        callback.onConvertDialogResult()
    }


    fun assert() {
        titleText.isDisplayed()
        messageText.isDisplayed()

        noButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        yesButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

    interface Callback {
        fun onConvertDialogResult()
    }

    companion object {
        operator fun invoke(func: ConvertDialogUi.() -> Unit,
                            noteItem: NoteItem, callback: Callback): ConvertDialogUi {
            return ConvertDialogUi(noteItem, callback).apply { waitOpen { assert() } }.apply(func)
        }
    }

}