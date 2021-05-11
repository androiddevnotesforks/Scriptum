package sgtmelon.safedialog.time

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import sgtmelon.extension.afterNow
import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getText
import sgtmelon.extension.is24Format
import sgtmelon.safedialog.BuildConfig
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.applyAnimation
import sgtmelon.safedialog.safeShow
import java.util.*
import kotlin.collections.ArrayList

/**
 * Dialog for choose time
 */
class TimeDialog : DateTimeBlankDialog(), ITimeDialog {

    private var dateList: ArrayList<String> = ArrayList()

    /**
     * Save item position in list for next operations.
     */
    var position: Int = NdValue.POSITION
        private set

    /**
     * Call before [safeShow].
     */
    fun setArguments(
        calendar: Calendar,
        dateList: List<String>,
        p: Int = NdValue.POSITION
    ) = apply {
        arguments = Bundle().apply {
            putLong(SavedTag.TIME, calendar.clearSeconds().timeInMillis)
            putStringArrayList(SavedTag.LIST, ArrayList(dateList))
            putInt(SavedTag.POSITION, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        if (BuildConfig.DEBUG) {
            callback = this
        }

        val changeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            setEnable()
        }

        return GoodTimePickerDialog(context as Context, this, changeListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            context.is24Format()
        ).applyAnimation()
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        super.onRestoreInstanceState(bundle)
        calendar.timeInMillis = bundle?.getLong(SavedTag.TIME) ?: defaultTime
        dateList = bundle?.getStringArrayList(SavedTag.LIST) ?: ArrayList()
        position = bundle?.getInt(SavedTag.POSITION) ?: NdValue.POSITION
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SavedTag.TIME, calendar.timeInMillis)
        outState.putStringArrayList(SavedTag.LIST, dateList)
        outState.putInt(SavedTag.POSITION, position)
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
        @VisibleForTesting
        var callback: ITimeDialog? = null

        /**
         * TODO #TEST write unit test
         */
        fun getPositiveEnabled(calendar: Calendar, dateList: List<String>): Boolean {
            return calendar.afterNow() && !dateList.contains(calendar.getText())
        }
    }
}