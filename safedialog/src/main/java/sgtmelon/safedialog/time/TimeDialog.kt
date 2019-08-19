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
 *
 * @author SerjantArbuz
 */
class TimeDialog : DateTimeBlankDialog() {

    private var dateList: ArrayList<String> = ArrayList()

    /**
     * Call before [show]
     */
    fun setArguments(calendar: Calendar, dateList: List<String>) = apply {
        calendar.set(Calendar.SECOND, 0)
        arguments = Bundle().apply {
            putLong(INIT, calendar.timeInMillis)
            putStringArrayList(VALUE, ArrayList(dateList))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(INIT) ?: bundle?.getLong(INIT)
                ?: defaultTime

        this.dateList = savedInstanceState?.getStringArrayList(VALUE)
                ?: bundle?.getStringArrayList(VALUE) ?: ArrayList()

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

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { putStringArrayList(VALUE, dateList) })

    /**
     * Check that date and time of [calendar] are not from the past
     */
    override fun setEnable() {
        super.setEnable()

        positiveButton?.isEnabled = calendar.afterNow()
                && !dateList.contains(getDateFormat().format(calendar.time))
    }

}