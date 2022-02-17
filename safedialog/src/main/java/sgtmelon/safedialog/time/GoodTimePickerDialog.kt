package sgtmelon.safedialog.time

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

/**
 * Version of standard [TimePickerDialog] but with [changeListener]
 */
internal class GoodTimePickerDialog(
    context: Context,
    listener: OnTimeSetListener,
    private val changeListener: OnTimeSetListener,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean
) : TimePickerDialog(context, listener, hourOfDay, minute, is24HourView) {

    init {
        try {
            val superClass = javaClass.superclass

            val timePickerField = superClass?.getDeclaredField("mTimePicker")
            timePickerField?.isAccessible = true

            val timePicker = timePickerField?.get(this) as? TimePicker
            timePicker?.setOnTimeChangedListener(this)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, minute)
        changeListener.onTimeSet(view, hourOfDay, minute)
    }
}