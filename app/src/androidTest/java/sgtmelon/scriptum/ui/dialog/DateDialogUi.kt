package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import java.util.*

/**
 * Class for UI control [DateDialog]
 */
class DateDialogUi : ParentUi(), IDialogUi {

    //region Views

    private val resetButton = getViewByText(R.string.dialog_button_reset)
    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    private val currentDate get() = Calendar.getInstance()

    // TODO #TEST create UI

    fun onClickReset() = waitClose { resetButton.click() }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose { applyButton.click() }

    fun assert(updateDate: Boolean) {
        resetButton.isDisplayed(updateDate).isEnabled()

        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled()
    }

    companion object {
        operator fun invoke(func: DateDialogUi.() -> Unit, updateDate: Boolean) =
                DateDialogUi().apply { waitOpen { assert(updateDate) } }.apply(func)
    }

}