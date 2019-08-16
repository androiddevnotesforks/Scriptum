package sgtmelon.safedialog.beta

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import java.util.*

/**
 * Dialog for choose time
 *
 * @author SerjantArbuz
 */
class TimeDialog : DateTimeBlankDialog() {

    // TODO #RELEASE1 add list with dates which can't be selected

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(VALUE) ?: bundle?.getLong(VALUE)
                ?: defaultTime

        val changeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            setEnable()
        }

        // TODO #RELEASE1 replace is24HourFormat with extension from [TimeExtension]
        return GoodTimePickerDialog(context as Context, this, changeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context as Context)
        )
    }

    /**
     * Check that date and time of [calendar] are not from the past
     */
    override fun setEnable() {
        super.setEnable()
        positiveButton?.isEnabled = calendar.after(Calendar.getInstance())
    }

}