package sgtmelon.safedialog.beta

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import sgtmelon.safedialog.DialogBlank
import sgtmelon.safedialog.R
import java.util.*

/**
 * Dialog for choose date
 *
 * @author SerjantArbuz
 */
class DateDialog : DialogBlank(), DatePickerDialog.OnDateSetListener {

    private val defaultTime get() = Calendar.getInstance().timeInMillis

    var calendar: Calendar = Calendar.getInstance()
        private set

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(VALUE, calendar.timeInMillis)
        })
    }

    /**
     * Change button text without override listener which call [onDateSet]
     */
    override fun setupButton() {
        super.setupButton()
        positiveButton?.text = getString(R.string.dialog_btn_accept)
        negativeButton?.text = getString(R.string.dialog_btn_cancel)
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

}