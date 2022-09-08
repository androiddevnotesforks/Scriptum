package sgtmelon.scriptum.cleanup.ui.dialog.time

import java.util.Calendar
import sgtmelon.safedialog.dialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control [TimeDialog]
 */
class TimeDialogUi(
    private val callback: DateTimeCallback,
    private val calendar: Calendar,
    private val dateList: List<String>
) : ParentUi(), IDialogUi {

    //region Views

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    //endregion

    fun onTime(min: Int) = apply {
        calendar.add(Calendar.MINUTE, min)

        TimeDialog.callback?.onTestUpdateTime(calendar)

        waitOperation { assert() }
    }

    fun onTime(calendar: Calendar) = apply {
        this.calendar.apply {
            set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        }

        TimeDialog.callback?.onTestUpdateTime(calendar)

        waitOperation { assert() }
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (!applyEnabled) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.onTimeDialogResult(calendar)
    }


    fun assert() {
        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled(applyEnabled) { withTextColor(R.attr.clAccent) }
    }

    private val applyEnabled get() = TimeDialog.getPositiveEnabled(calendar, dateList)

    companion object {
        operator fun invoke(
            func: TimeDialogUi.() -> Unit,
            calendar: Calendar,
            dateList: List<String>,
            callback: DateTimeCallback
        ): TimeDialogUi {
            return TimeDialogUi(callback, calendar, dateList)
                .apply { waitOpen { assert() } }
                .apply(func)
        }
    }
}