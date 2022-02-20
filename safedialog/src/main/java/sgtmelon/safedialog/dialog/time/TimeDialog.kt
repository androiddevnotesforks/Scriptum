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
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.dialog.parent.BlankDateTimeDialog
import sgtmelon.safedialog.dialog.callback.TimeTestCallback
import sgtmelon.safedialog.dialog.parent.TouchTimePickerDialog
import sgtmelon.safedialog.utils.safeShow
import java.util.*
import kotlin.collections.ArrayList

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        if (BuildConfig.DEBUG) {
            callback = this
        }

        return TouchTimePickerDialog(requireContext(), this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            context.is24Format(),
            touchChangeListener
        ).applyAnimation()
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

        /**
         * TODO #TEST write unit test
         */
        fun getPositiveEnabled(calendar: Calendar, dateList: List<String>): Boolean {
            return calendar.afterNow() && !dateList.contains(calendar.getText())
        }
    }
}