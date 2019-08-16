package sgtmelon.safedialog.beta

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker

/**
 * Version of standard [TimePickerDialog] but with working listener
 *
 * @author SerjantArbuz
 */
class GoodTimePickerDialog(
        context: Context,
        private  val listener: OnTimeSetListener,
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
            e.printStackTrace();
        } catch (e : IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onTimeChanged(view: TimePicker?, hourOfDay: Int, minute: Int) {
        super.onTimeChanged(view, hourOfDay, minute)
        listener.onTimeSet(view, hourOfDay, minute)
    }

}