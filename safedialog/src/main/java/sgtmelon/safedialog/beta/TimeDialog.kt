package sgtmelon.safedialog.beta

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import sgtmelon.safedialog.DialogBlank
import java.util.*

/**
 * Диалог для выбора времени
 *
 * @author SerjantArbuz
 */
class TimeDialog : DialogBlank() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val dialog = TimePickerDialog(context as Context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // TODO
        )

//        dialog.ti.minDate = Calendar.getInstance().timeInMillis

        return dialog
    }

}