package sgtmelon.safedialog.dialog.time

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import sgtmelon.common.utils.afterNow
import sgtmelon.common.utils.clearSeconds
import sgtmelon.common.utils.getText
import sgtmelon.common.utils.is24Format
import sgtmelon.common.test.annotation.RunNone
import sgtmelon.safedialog.BuildConfig
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.dialog.parent.BlankDateTimeDialog
import sgtmelon.safedialog.dialog.callback.ITimeDialog
import sgtmelon.safedialog.utils.safeShow
import java.util.*
import kotlin.collections.ArrayList

/**
 * Dialog for choose time
 */
class TimeDialog : BlankDateTimeDialog(), ITimeDialog {

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

            changeButtonEnable()
        }

        return GoodTimePickerDialog(requireContext(), this, changeListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            context.is24Format()
        ).applyAnimation()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)
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

    override fun changeButtonEnable() {
        super.changeButtonEnable()

        /**
         * Check that date and time of [calendar] are not from the past
         */
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
        @RunNone var callback: ITimeDialog? = null

        /**
         * TODO #TEST write unit test
         */
        fun getPositiveEnabled(calendar: Calendar, dateList: List<String>): Boolean {
            return calendar.afterNow() && !dateList.contains(calendar.getText())
        }
    }
}