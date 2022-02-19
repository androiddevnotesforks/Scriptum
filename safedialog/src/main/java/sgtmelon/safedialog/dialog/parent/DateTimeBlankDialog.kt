package sgtmelon.safedialog.dialog.parent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.widget.DatePicker
import android.widget.TimePicker
import sgtmelon.common.clearSeconds
import sgtmelon.common.getNewCalendar
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.R
import java.util.*

/**
 * Base class for [DateDialog] and [TimeDialog]
 */
abstract class DateTimeBlankDialog : BlankDialog(),
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    protected val defaultTime get() = getNewCalendar().clearSeconds().timeInMillis

    var calendar: Calendar = getNewCalendar().clearSeconds()
        private set

    /**
     * Change button text without override listener which call [onTimeSet]
     */
    override fun setupButton() {
        super.setupButton()
        positiveButton?.text = getString(R.string.dialog_button_apply)
        negativeButton?.text = getString(R.string.dialog_button_cancel)
    }

    /**
     * This func calls after [positiveButton] click
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        onPositiveClick.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

    /**
     * This func calls after [positiveButton] click
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        onPositiveClick.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

}