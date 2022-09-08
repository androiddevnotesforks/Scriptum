package sgtmelon.safedialog.dialog.parent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.Calendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.safedialog.R
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.safedialog.dialog.time.TimeDialog

/**
 * Base class for work with [DateDialog] and [TimeDialog].
 */
abstract class BlankDateTimeDialog : BlankDialog(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    protected val defaultTime get() = getClearCalendar().timeInMillis

    var calendar: Calendar = getClearCalendar()
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
     * This func calls after [positiveButton] click.
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        onPositiveClick.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

    /**
     * This func calls after [positiveButton] click.
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        onPositiveClick.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }
}