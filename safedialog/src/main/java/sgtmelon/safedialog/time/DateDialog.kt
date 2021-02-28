package sgtmelon.safedialog.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import sgtmelon.extension.clearSeconds
import sgtmelon.safedialog.BuildConfig
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.applyAnimation
import java.util.*

/**
 * Dialog for choose date
 */
class DateDialog : DateTimeBlankDialog(), IDateDialog {

    var neutralListener: DialogInterface.OnClickListener? = null

    private var neutralVisible: Boolean = NdValue.KEY

    /**
     * Save item position in list for next operations
     */
    var position: Int = NdValue.POSITION
        private set

    /**
     * Call before [show].
     */
    fun setArguments(
        calendar: Calendar,
        neutralVisible: Boolean,
        p: Int = NdValue.POSITION
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

        return DatePickerDialog(context as Context, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = defaultTime
            setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_button_reset), neutralListener)
        }.applyAnimation()
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        super.onRestoreInstanceState(bundle)
        calendar.timeInMillis = bundle?.getLong(SavedTag.TIME) ?: defaultTime
        neutralVisible = bundle?.getBoolean(SavedTag.VISIBLE) ?: NdValue.KEY
        position = bundle?.getInt(SavedTag.POSITION) ?: NdValue.POSITION
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
        @VisibleForTesting
        var callback: IDateDialog? = null
    }

}