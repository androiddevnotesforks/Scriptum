package sgtmelon.scriptum.ui.dialog.time

import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getNewCalendar
import sgtmelon.safedialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.withTextColor
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class for UI control [DateDialog]
 */
class DateDialogUi(
        private val callback: DateTimeCallback,
        private val updateDate: Boolean
) : ParentUi(),
        IDialogUi {

    //region Views

    private val resetButton = getViewByText(R.string.dialog_button_reset)
    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    private val calendar = getNewCalendar().clearSeconds()

    fun onDate(day: Int) = apply {
        calendar.add(Calendar.DAY_OF_YEAR, day)

        DateDialog.callback?.updateDate(calendar)

        waitOperation { assert() }
    }

    fun onDate(calendar: Calendar) = apply {
        this.calendar.apply {
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
        }

        DateDialog.callback?.updateDate(calendar)

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
        resetButton.isDisplayed(updateDate).isEnabled().withTextColor(R.attr.clAccent)

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

    companion object {
        operator fun invoke(func: DateDialogUi.() -> Unit, updateDate: Boolean,
                            callback: DateTimeCallback): DateDialogUi {
            return DateDialogUi(callback, updateDate).apply { waitOpen { assert() } }.apply(func)
        }
    }

}