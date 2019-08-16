package sgtmelon.safedialog.beta

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import sgtmelon.safedialog.DialogBlank
import sgtmelon.safedialog.R
import java.util.*

/**
 * Dialog for choose time
 *
 * @author SerjantArbuz
 */
class TimeDialog : DialogBlank(), TimePickerDialog.OnTimeSetListener {

    private val defaultTime get() = Calendar.getInstance().timeInMillis

    var calendar: Calendar = Calendar.getInstance()
        private set

    /**
     * Call before [show]
     */
    fun setArguments(calendar: Calendar) = apply {
        calendar.set(Calendar.SECOND, 0)
        arguments = Bundle().apply { putLong(VALUE, calendar.timeInMillis) }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        calendar.timeInMillis = savedInstanceState?.getLong(VALUE) ?: bundle?.getLong(VALUE)
                ?: defaultTime

        val changeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            setEnable()
        }

        // TODO #RELEASE replace is24HourFormat with extension from [TimeExtension]
        return GoodTimePickerDialog(context as Context, this, changeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context as Context)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putLong(VALUE, calendar.timeInMillis)
        })
    }

    /**
     * Change button text without override listener which call [onTimeSet]
     */
    override fun setupButton() {
        super.setupButton()
        positiveButton?.text = getString(R.string.dialog_btn_accept)
        negativeButton?.text = getString(R.string.dialog_btn_cancel)
    }

    /**
     * Check that date and time of [calendar] are not from the past
     */
    override fun setEnable() {
        super.setEnable()
        positiveButton?.isEnabled = calendar.after(Calendar.getInstance())
    }

    /**
     * This func calls after [positiveButton] click
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        onPositiveClick.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
    }

}