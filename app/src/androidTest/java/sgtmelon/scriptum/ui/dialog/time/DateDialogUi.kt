package sgtmelon.scriptum.ui.dialog.time

import java.util.Calendar
import sgtmelon.common.utils.clearSeconds
import sgtmelon.common.utils.getNewCalendar
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control [DateDialog]
 */
class DateDialogUi(
    private val callback: DateTimeCallback,
    private val isUpdateDate: Boolean
) : ParentUi(),
        IDialogUi {

    //region Views

    private val resetButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_reset)
    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    //endregion

    private val calendar = getNewCalendar().clearSeconds()

    fun onDate(day: Int) = apply {
        calendar.add(Calendar.DAY_OF_YEAR, day)

        DateDialog.callback?.onTestUpdateDate(calendar)

        waitOperation { assert() }
    }

    fun onDate(calendar: Calendar) = apply {
        this.calendar.apply {
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
        }

        DateDialog.callback?.onTestUpdateDate(calendar)

        waitOperation { assert() }
    }

    fun onClickReset() {
        waitClose { resetButton.click() }
        callback.onDateDialogResetResult()
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply(dateList: List<String> = ArrayList(),
                     func: TimeDialogUi.() -> Unit = {}) = waitClose {
        applyButton.click()
        TimeDialogUi(func, calendar, dateList, callback)
    }


    fun assert() {
        resetButton.isDisplayed(isUpdateDate).isEnabled().withTextColor(R.attr.clAccent)

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

    companion object {
        operator fun invoke(
            func: DateDialogUi.() -> Unit,
            isUpdateDate: Boolean,
            callback: DateTimeCallback
        ): DateDialogUi {
            return DateDialogUi(callback, isUpdateDate).apply { waitOpen { assert() } }.apply(func)
        }
    }
}