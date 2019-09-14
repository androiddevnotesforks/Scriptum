package sgtmelon.safedialog.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import sgtmelon.safedialog.R
import java.util.*

/**
 * Dialog for choose date
 */
class DateDialog : DateTimeBlankDialog() {

    var neutralListener: DialogInterface.OnClickListener? = null

    private var neutralVisible: Boolean = false

    /**
     * Call before [show]
     */
    fun setArguments(calendar: Calendar, neutralVisible: Boolean) = apply {
        arguments = Bundle().apply {
            putLong(INIT, calendar.apply { set(Calendar.SECOND, 0) }.timeInMillis)
            putBoolean(VALUE, neutralVisible)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar.timeInMillis = savedInstanceState?.getLong(INIT) ?: arguments?.getLong(INIT)
                ?: defaultTime

        neutralVisible = savedInstanceState?.getBoolean(VALUE) ?: arguments?.getBoolean(VALUE)
                ?: false

        return DatePickerDialog(context as Context, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = defaultTime

            setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_button_reset), neutralListener)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(INIT, calendar.timeInMillis)
            putBoolean(VALUE, neutralVisible)
        })
    }

    override fun setupButton() {
        super.setupButton()
        neutralButton?.visibility = if (neutralVisible) View.VISIBLE else View.GONE
    }

}