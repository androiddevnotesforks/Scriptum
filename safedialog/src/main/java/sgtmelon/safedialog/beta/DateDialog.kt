package sgtmelon.safedialog.beta

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import sgtmelon.safedialog.DialogBlank
import java.util.*

/**
 * Диалог для выбора даты
 *
 * @author SerjantArbuz
 */
class DateDialog : DialogBlank() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(context as Context,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth -> },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.datePicker.minDate = Calendar.getInstance().timeInMillis

        return dialog
    }

}