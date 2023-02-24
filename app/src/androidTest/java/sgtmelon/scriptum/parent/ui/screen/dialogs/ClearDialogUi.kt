package sgtmelon.scriptum.parent.ui.screen.dialogs

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [MessageDialog] when clear bin.
 */
class ClearDialogUi : UiPart(),
    DialogUi {

    private val titleText = getViewByText(R.string.dialog_title_clear_bin)
    private val messageText = getViewByText(R.string.dialog_text_clear_bin)
    private val noButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_no)
    private val yesButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_yes)

    fun no() = waitClose { noButton.click() }

    fun yes() = waitClose { yesButton.click() }

    fun assert() {
        titleText.isDisplayed().withTextColor(R.attr.clContent)
        messageText.isDisplayed().withTextColor(R.attr.clContent)
        noButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        yesButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

    companion object {
        inline operator fun invoke(func: ClearDialogUi.() -> Unit): ClearDialogUi {
            return ClearDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}