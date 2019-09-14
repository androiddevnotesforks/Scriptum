package sgtmelon.safedialog.time

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import sgtmelon.extension.afterNow
import sgtmelon.extension.getDateFormat
import sgtmelon.extension.is24Format
import java.util.*
import kotlin.collections.ArrayList

/**
 * Dialog for choose time
 */
class TimeDialog : DateTimeBlankDialog() {

    private var dateList: ArrayList<String> = ArrayList()

    /**
     * Call before [show]
     */
    fun setArguments(calendar: Calendar, dateList: List<String>) = apply {
        arguments = Bundle().apply {
            putLong(INIT, calendar.apply { set(Calendar.SECOND, 0) }.timeInMillis)
            putStringArrayList(VALUE, ArrayList(dateList))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar.timeInMillis = savedInstanceState?.getLong(INIT)
                ?: arguments?.getLong(INIT) ?: defaultTime

        dateList = savedInstanceState?.getStringArrayList(VALUE)
                ?: arguments?.getStringArrayList(VALUE) ?: ArrayList()

        val changeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            setEnable()
        }

        return GoodTimePickerDialog(context as Context, this, changeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                context.is24Format()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(INIT, calendar.timeInMillis)
            putStringArrayList(VALUE, dateList)
        })
    }

    /**
     * Check that date and time of [calendar] are not from the past
     */
    override fun setEnable() {
        super.setEnable()

        positiveButton?.isEnabled = calendar.afterNow()
                && !dateList.contains(getDateFormat().format(calendar.time))
    }

}