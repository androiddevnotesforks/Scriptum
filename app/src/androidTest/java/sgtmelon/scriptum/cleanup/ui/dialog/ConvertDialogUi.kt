package sgtmelon.scriptum.cleanup.ui.dialog

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.click
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentUi

/**
 * Class for UI control of [MessageDialog] which open from [NoteActivity] on convert.
 */
class ConvertDialogUi(item: NoteItem, private val callback: Callback) : ParentUi(), IDialogUi {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_convert)
    private val messageText = getViewByText(when (item.type) {
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
        operator fun invoke(
            func: ConvertDialogUi.() -> Unit,
            item: NoteItem,
            callback: Callback
        ): ConvertDialogUi {
            return ConvertDialogUi(item, callback).apply { waitOpen { assert() } }.apply(func)
        }
    }
}