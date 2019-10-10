package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control [TimeDialog]
 */
class TimeDialogUi : ParentUi(), IDialogUi {

    //region Views

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    // TODO #TEST create UI

    fun assert() {
        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled(TODO ("#TEST pass here boolean"))
    }

}