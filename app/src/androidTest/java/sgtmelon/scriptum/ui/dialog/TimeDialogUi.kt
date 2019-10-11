package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class for UI control [TimeDialog]
 */
class TimeDialogUi : ParentUi(), IDialogUi {

    //region Views

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    private val currentTime get() = Calendar.getInstance()

    // TODO #TEST create UI

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose { applyButton.click() }


    fun assert(dateList: List<String> = ArrayList()) {
        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled(TimeDialog.getPositiveEnabled(currentTime, dateList))
    }

    companion object {
        operator fun invoke(func: TimeDialogUi.() -> Unit) =
                TimeDialogUi().apply { waitOpen { assert() } }.apply(func)
    }

}