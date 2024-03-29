package sgtmelon.scriptum.source.ui.screen.dialogs.time

import java.util.Calendar
import sgtmelon.safedialog.dialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control [TimeDialog].
 */
class TimeDialogUi(
    private val callback: DateTimeCallback,
    private val calendar: Calendar,
    private val dateList: List<String>
) : UiPart(),
    DialogUi {

    //region Views

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    //endregion

    fun set(addMin: Int) = apply {
        calendar.add(Calendar.MINUTE, addMin)

        TimeDialog.callback?.onTestUpdateTime(calendar)

        waitOperation { assert() }
    }

    fun set(calendar: Calendar) = apply {
        this.calendar.apply {
            set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        }

        TimeDialog.callback?.onTestUpdateTime(calendar)

        waitOperation { assert() }
    }

    fun cancel() = waitClose { cancelButton.click() }

    fun applyTime() = waitClose {
        if (!applyEnabled) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.timeSetResult(calendar)
    }


    fun assert() {
        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled(applyEnabled) { withTextColor(R.attr.clAccent) }
    }

    private val applyEnabled get() = TimeDialog.getPositiveEnabled(calendar, dateList)

    companion object {
        inline operator fun invoke(
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