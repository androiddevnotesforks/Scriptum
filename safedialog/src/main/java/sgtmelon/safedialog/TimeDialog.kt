package sgtmelon.safedialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import java.util.*

/**
 * Диалог для выбора времени
 *
 * @author SerjantArbuz
 */
class TimeDialog : DialogBlank() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val dialog = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // TODO
        )

//        dialog.ti.minDate = Calendar.getInstance().timeInMillis

        return dialog
    }

}