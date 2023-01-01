package sgtmelon.scriptum.cleanup.ui.dialog

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.type
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [MessageDialog] which open from [NoteActivity] on convert.
 */
class ConvertDialogUi(item: NoteItem, private val callback: Callback) : ParentScreen(), DialogUi {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_convert)
    private val messageText = getViewByText(
        when (item.type) {
            NoteType.TEXT -> R.string.dialog_text_convert_text
            NoteType.ROLL -> R.string.dialog_roll_convert_roll
        }
    )

    private val noButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_no)
    private val yesButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_yes)

    //endregion

    fun onClickNo() = waitClose { noButton.click() }

    fun onClickYes() = waitClose {
        yesButton.click()
        callback.onConvertDialogResult()
    }


    fun assert() {
        titleText.isDisplayed().withTextColor(R.attr.clContent)
        messageText.isDisplayed().withTextColor(R.attr.clContent)

        noButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        yesButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

    interface Callback {
        fun onConvertDialogResult()
    }

    companion object {
        inline operator fun invoke(
            func: ConvertDialogUi.() -> Unit,
            item: NoteItem,
            callback: Callback
        ): ConvertDialogUi {
            return ConvertDialogUi(item, callback).apply { waitOpen { assert() } }.apply(func)
        }
    }
}