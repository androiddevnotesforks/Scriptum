package sgtmelon.safedialog.time

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import sgtmelon.extension.afterNow
import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getString
import sgtmelon.extension.is24Format
import sgtmelon.safedialog.BuildConfig
import java.util.*
import kotlin.collections.ArrayList

/**
 * Dialog for choose time
 */
class TimeDialog : DateTimeBlankDialog(), ITimeDialog {

    private var dateList: ArrayList<String> = ArrayList()

    /**
     * Save item position in list for next operations
     */
    var position: Int = ND_POSITION
        private set

    /**
     * Call before [show]
     */
    fun setArguments(calendar: Calendar, dateList: List<String>,
                     p: Int = ND_POSITION) = apply {
        arguments = Bundle().apply {
            putLong(INIT, calendar.clearSeconds().timeInMillis)
            putStringArrayList(VALUE, ArrayList(dateList))
            putInt(POSITION, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (BuildConfig.DEBUG) callback = this

        calendar.timeInMillis = savedInstanceState?.getLong(INIT)
                ?: arguments?.getLong(INIT) ?: defaultTime

        dateList = savedInstanceState?.getStringArrayList(VALUE)
                ?: arguments?.getStringArrayList(VALUE) ?: ArrayList()

        position = savedInstanceState?.getInt(POSITION)
                ?: arguments?.getInt(POSITION) ?: ND_POSITION

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
            putInt(POSITION, position)
        })
    }

    /**
     * Check that date and time of [calendar] are not from the past
     */
    override fun setEnable() {
        super.setEnable()

        positiveButton?.isEnabled = getPositiveEnabled(calendar, dateList)
    }

    override fun updateTime(calendar: Calendar) {
        activity?.runOnUiThread {
            (dialog as? TimePickerDialog)?.updateTime(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
            )
        }
    }

    companion object {
        var callback: ITimeDialog? = null

        /**
         * TODO #TEST write unit test
         */
        fun getPositiveEnabled(calendar: Calendar, dateList: List<String>) : Boolean{
            return calendar.afterNow() && !dateList.contains(calendar.getString())
        }
    }

}