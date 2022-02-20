package sgtmelon.safedialog.dialog.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import sgtmelon.common.utils.clearSeconds
import sgtmelon.common.test.annotation.RunNone
import sgtmelon.safedialog.BuildConfig
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.dialog.parent.BlankDateTimeDialog
import sgtmelon.safedialog.dialog.callback.IDateDialog
import sgtmelon.safedialog.utils.safeShow
import java.util.*

/**
 * Dialog for choose date
 */
class DateDialog : BlankDateTimeDialog(),
    IDateDialog {

    var neutralListener: DialogInterface.OnClickListener? = null

    private var neutralVisible: Boolean = NO_NEUTRAL

    /**
     * Save note item position in list for next operations. [NO_POSITION] if dialog open
     * happened not from list.
     */
    var position: Int = NO_POSITION
        private set

    /**
     * Call before [safeShow].
     */
    fun setArguments(
        calendar: Calendar,
        neutralVisible: Boolean,
        p: Int = NO_POSITION
    ) = apply {
        arguments = Bundle().apply {
            putLong(SavedTag.TIME, calendar.clearSeconds().timeInMillis)
            putBoolean(SavedTag.VISIBLE, neutralVisible)
            putInt(SavedTag.POSITION, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        if (BuildConfig.DEBUG) {
            callback = this
        }

        return DatePickerDialog(requireContext(), this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = defaultTime
            setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_button_reset), neutralListener)
        }.applyAnimation()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)
        calendar.timeInMillis = bundle?.getLong(SavedTag.TIME) ?: defaultTime
        neutralVisible = bundle?.getBoolean(SavedTag.VISIBLE) ?: NdValue.KEY
        position = bundle?.getInt(SavedTag.POSITION) ?: NO_POSITION
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SavedTag.TIME, calendar.timeInMillis)
        outState.putBoolean(SavedTag.VISIBLE, neutralVisible)
        outState.putInt(SavedTag.POSITION, position)
    }

    override fun setupButton() {
        super.setupButton()

        neutralButton?.visibility = if (neutralVisible) View.VISIBLE else View.GONE
    }

    override fun updateDate(calendar: Calendar) {
        activity?.runOnUiThread {
            (dialog as? DatePickerDialog)?.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    companion object {
        private const val NO_POSITION = -1
        private const val NO_NEUTRAL = false

        @RunNone var callback: IDateDialog? = null
    }
}