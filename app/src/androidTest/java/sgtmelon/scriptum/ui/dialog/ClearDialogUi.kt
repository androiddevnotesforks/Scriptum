package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.click
import sgtmelon.scriptum.ui.basic.isDisplayed
import sgtmelon.scriptum.ui.basic.isEnabled

/**
 * Class for UI control of [MessageDialog] when clear bin
 */
class ClearDialogUi : ParentDialogUi() {

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_clear_bin)
    private val messageText = getViewByText(R.string.dialog_text_clear_bin)

    private val noButton = getViewByText(R.string.dialog_button_no)
    private val yesButton = getViewByText(R.string.dialog_button_yes)

    //endregion

    fun onClickNo() = waitClose { noButton.click() }

    fun onClickYes() = waitClose { yesButton.click() }


    fun assert() {
        titleText.isDisplayed()
        messageText.isDisplayed()

        noButton.isDisplayed().isEnabled()
        yesButton.isDisplayed().isEnabled()
    }

    companion object {
        operator fun invoke(func: ClearDialogUi.() -> Unit) =
                ClearDialogUi().apply { waitOpen { assert() } }.apply(func)
    }

}