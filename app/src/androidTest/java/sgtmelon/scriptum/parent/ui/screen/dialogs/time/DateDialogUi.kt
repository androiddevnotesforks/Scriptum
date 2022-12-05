package sgtmelon.scriptum.parent.ui.screen.dialogs.time

import java.util.Calendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.scriptum.parent.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control [DateDialog].
 */
class DateDialogUi(
    private val callback: DateTimeCallback,
    private val isUpdateDate: Boolean
) : UiPart(),
    DialogUi {

    //region Views

    private val resetButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_reset)
    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    //endregion

    private val calendar = getClearCalendar()

    fun set(addDay: Int) = apply {
        calendar.add(Calendar.DAY_OF_YEAR, addDay)

        DateDialog.callback?.onTestUpdateDate(calendar)

        waitOperation { assert() }
    }

    fun set(calendar: Calendar) = apply {
        this.calendar.apply {
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
        }

        DateDialog.callback?.onTestUpdateDate(calendar)

        waitOperation { assert() }
    }

    fun reset() {
        waitClose { resetButton.click() }
        callback.dateResetResult()
    }

    fun cancel() = waitClose { cancelButton.click() }

    fun applyDate(
        dateList: List<String> = ArrayList(),
        func: TimeDialogUi.() -> Unit = {}
    ) = waitClose {
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