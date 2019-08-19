package sgtmelon.safedialog.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import java.util.*

/**
 * Dialog for choose date
 *
 * @author SerjantArbuz
 */
class DateDialog : DateTimeBlankDialog() {

    // TODO #RELEASE1 add neutral button for delete alarm

    /**
     * Call before [show]
     */
    fun setArguments(calendar: Calendar) = apply {
        calendar.set(Calendar.SECOND, 0)
        arguments = Bundle().apply { putLong(VALUE, calendar.timeInMillis) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(VALUE) ?: bundle?.getLong(VALUE)
                ?: defaultTime

        return DatePickerDialog(context as Context, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).apply { datePicker.minDate = defaultTime }
    }

}