package sgtmelon.scriptum.cleanup.ui.dialog.time

import java.util.Calendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.ui.testing.screen.parent.ParentScreen
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control [DateDialog]
 */
class DateDialogUi(
    private val callback: DateTimeCallback,
    private val isUpdateDate: Boolean
) : ParentScreen(),
    IDialogUi {

    //region Views

    private val resetButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_reset)
    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    //endregion

    private val calendar = getClearCalendar()

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

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
    }

    companion object {
        inline operator fun invoke(
            func: DateDialogUi.() -> Unit,
            isUpdateDate: Boolean,
            callback: DateTimeCallback
        ): DateDialogUi {
            return DateDialogUi(callback, isUpdateDate).apply { waitOpen { assert() } }.apply(func)
        }
    }
}