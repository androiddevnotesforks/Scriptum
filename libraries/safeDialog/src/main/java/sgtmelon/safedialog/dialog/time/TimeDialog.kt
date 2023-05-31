package sgtmelon.safedialog.dialog.time

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import java.util.Calendar
import sgtmelon.extensions.clearSeconds
import sgtmelon.extensions.is24HourFormat
import sgtmelon.extensions.isAfterNow
import sgtmelon.extensions.toText
import sgtmelon.safedialog.BuildConfig
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.callback.TimeTestCallback
import sgtmelon.safedialog.dialog.parent.BlankDateTimeDialog
import sgtmelon.safedialog.dialog.parent.TouchTimePickerDialog
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.test.prod.RunNone

/**
 * Dialog for choose time
 */
class TimeDialog : BlankDateTimeDialog(), TimeTestCallback {

    private var dateList: ArrayList<String> = ArrayList()

    /**
     * Save note item position in list for next operations. [DEF_POSITION] - if dialog open
     * happened not from list.
     */
    var position: Int = DEF_POSITION
        private set

    private val touchChangeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        changeButtonEnable()
    }

    /**
     * Call before [safeShow].
     *
     * [calendar] - for set current time
     * [dateList] - contains dates which not available for choose
     * [p] - position of item in list
     */
    fun setArguments(
        calendar: Calendar,
        dateList: List<String>,
        p: Int = DEF_POSITION
    ) = apply {
        arguments = Bundle().apply {
            putLong(SavedTag.Time.VALUE, calendar.clearSeconds().timeInMillis)
            putStringArrayList(SavedTag.Time.LIST, ArrayList(dateList))
            putInt(SavedTag.Time.POSITION, p)
        }
    }

    override fun createDialog(context: Context): Dialog {
        if (BuildConfig.DEBUG) {
            callback = this
        }

        return TouchTimePickerDialog(
            context, this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            context.is24HourFormat,
            touchChangeListener
        )
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        calendar.timeInMillis = bundle?.getLong(SavedTag.Time.VALUE) ?: defaultTime
        dateList = bundle?.getStringArrayList(SavedTag.Time.LIST) ?: ArrayList()
        position = bundle?.getInt(SavedTag.Time.POSITION) ?: DEF_POSITION
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(SavedTag.Time.VALUE, calendar.timeInMillis)
        outState.putStringArrayList(SavedTag.Time.LIST, dateList)
        outState.putInt(SavedTag.Time.POSITION, position)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()

        /**
         * Check that date and time of [calendar] are not from the past
         */
        positiveButton?.isEnabled = getPositiveEnabled(calendar, dateList)
    }

    override fun onTestUpdateTime(calendar: Calendar) {
        activity?.runOnUiThread {
            (dialog as? TimePickerDialog)?.updateTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        }
    }

    companion object {
        @RunNone var callback: TimeTestCallback? = null

        private const val DEF_POSITION = -1

        fun getPositiveEnabled(calendar: Calendar, dateList: List<String>): Boolean {
            return calendar.isAfterNow && !dateList.contains(calendar.toText())
        }
    }
}