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

    private var calendar: Calendar = Calendar.getInstance()

    fun setArguments(calendar: Calendar) = apply {
        arguments = Bundle().apply { putLong(VALUE, calendar.timeInMillis) }
    }

    fun getResult(): Calendar {
        val d = (dialog as? DatePickerDialog)?.datePicker
        return calendar.apply { d?.let { set(it.year, it.month, it.dayOfMonth) } }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(VALUE) ?: bundle?.getLong(VALUE)
                ?: defaultTime

        return DatePickerDialog(context as Context, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_btn_accept), onPositiveClick)
            setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }

            datePicker.minDate = defaultTime
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(VALUE, calendar.timeInMillis)
        })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {}

}