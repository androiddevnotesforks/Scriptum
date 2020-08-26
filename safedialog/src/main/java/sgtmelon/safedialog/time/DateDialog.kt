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
import sgtmelon.safedialog.applyAnimation
import java.util.*

/**
 * Dialog for choose date
 */
class DateDialog : DateTimeBlankDialog(), IDateDialog {

    var neutralListener: DialogInterface.OnClickListener? = null

    private var neutralVisible: Boolean = false

    /**
     * Save item position in list for next operations
     */
    var position: Int = ND_POSITION
        private set

    /**
     * Call before [show]
     */
    fun setArguments(
        calendar: Calendar, neutralVisible: Boolean,
        p: Int = ND_POSITION
    ) = apply {
        arguments = Bundle().apply {
            putLong(INIT, calendar.clearSeconds().timeInMillis)
            putBoolean(VALUE, neutralVisible)
            putInt(POSITION, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (BuildConfig.DEBUG) callback = this

        calendar.timeInMillis = savedInstanceState?.getLong(INIT) ?: arguments?.getLong(INIT)
                ?: defaultTime

        neutralVisible = savedInstanceState?.getBoolean(VALUE) ?: arguments?.getBoolean(VALUE)
                ?: false

        position = savedInstanceState?.getInt(POSITION)
                ?: arguments?.getInt(POSITION) ?: ND_POSITION

        return DatePickerDialog(context as Context, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = defaultTime
            setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.dialog_button_reset), neutralListener)
        }.applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(INIT, calendar.timeInMillis)
            putBoolean(VALUE, neutralVisible)
            putInt(POSITION, position)
        })
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