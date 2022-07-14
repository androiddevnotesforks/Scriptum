package sgtmelon.safedialog.dialog.parent

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

/**
 * Version of standard [TimePickerDialog] but with [onTouchListener]. It need for update
 * out buttons while user touch/move time picker (not just set time).
 */
internal class TouchTimePickerDialog(
    context: Context,
    listener: OnTimeSetListener,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean,
    private val onTouchListener: OnTimeSetListener
) : TimePickerDialog(context, listener, hourOfDay, minute, is24HourView) {

    init {
        try {
            val superClass = javaClass.superclass

            val timePickerField = superClass?.getDeclaredField("mTimePicker")
            timePickerField?.isAccessible = true

            val timePicker = timePickerField?.get(this) as? TimePicker
            timePicker?.setOnTimeChangedListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, minute)
        onTouchListener.onTimeSet(view, hourOfDay, minute)
    }
}