package sgtmelon.safedialog.beta

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import sgtmelon.safedialog.DialogBlank
import sgtmelon.safedialog.R
import java.util.*

/**
 * Dialog for choose time
 *
 * @author SerjantArbuz
 */
class TimeDialog : DialogBlank(), TimePickerDialog.OnTimeSetListener {

    private val defaultTime get() = Calendar.getInstance().timeInMillis

    var calendar: Calendar = Calendar.getInstance()
        private set

    fun getResult(): Calendar {
        return calendar
    }

    fun setArguments(calendar: Calendar) = apply {
        arguments = Bundle().apply { putLong(VALUE, calendar.timeInMillis) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(VALUE) ?: bundle?.getLong(VALUE)
                ?: defaultTime

        return GoodTimePickerDialog(context as Context, this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // TODO #RELEASE
        ).apply {
            setButton(DialogInterface.BUTTON_POSITIVE, "") { _, _ -> }
            setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_btn_accept), positiveListener)
            setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(VALUE, calendar.timeInMillis)
        })
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.i("HERE", "hour = $hourOfDay | min = $minute")
    }

}