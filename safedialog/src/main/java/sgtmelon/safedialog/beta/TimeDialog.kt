package sgtmelon.safedialog.beta

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import sgtmelon.safedialog.DialogBlank
import sgtmelon.safedialog.R
import java.util.*

/**
 * Dialog for choose time
 *
 * @author SerjantArbuz
 */
class TimeDialog : DialogBlank() {

    private val defaultTime get() = Calendar.getInstance().timeInMillis

    var calendar: Calendar = Calendar.getInstance()
        private set

    fun setArguments(calendar: Calendar) = apply {
        arguments = Bundle().apply { putLong(VALUE, calendar.timeInMillis) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(INIT) ?: bundle?.getLong(INIT)
                ?: defaultTime

        return TimePickerDialog(context as Context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // TODO #RELEASE
        ).apply {
            setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_btn_accept), onPositiveClick)
            setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(VALUE, calendar.timeInMillis)
        })
    }

}