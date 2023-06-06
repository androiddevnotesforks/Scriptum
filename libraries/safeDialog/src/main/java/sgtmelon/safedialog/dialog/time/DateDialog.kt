package sgtmelon.safedialog.dialog.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import java.util.Calendar
import sgtmelon.extensions.clearSeconds
import sgtmelon.safedialog.BuildConfig
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.callback.DateTestCallback
import sgtmelon.safedialog.dialog.parent.BlankDateTimeDialog
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.test.prod.RunNone

/**
 * Dialog for choose date
 */
class DateDialog : BlankDateTimeDialog(),
    DateTestCallback {

    private var neutralVisible: Boolean = DEF_NEUTRAL

    /**
     * Save note item position in list for next operations. [DEF_POSITION] - if dialog open
     * happened not from list.
     */
    var position: Int = DEF_POSITION
        private set

    /**
     * Call before [safeShow].
     *
     * [calendar] - for set current time
     * [neutralVisible] - show or hide [neutralButton]/reset button.
     * [p] - position of item in list
     */
    fun setArguments(
        calendar: Calendar,
        neutralVisible: Boolean,
        p: Int = DEF_POSITION
    ) = apply {
        arguments = Bundle().apply {
            putLong(SavedTag.Date.VALUE, calendar.clearSeconds().timeInMillis)
            putBoolean(SavedTag.Date.VISIBLE, neutralVisible)
            putInt(SavedTag.Date.POSITION, p)
        }
    }

    override fun createDialog(context: Context): Dialog {
        if (BuildConfig.DEBUG) {
            callback = this
        }

        return DatePickerDialog(
            context, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = defaultTime
            setButton(
                DialogInterface.BUTTON_NEUTRAL,
                getString(R.string.dialog_button_reset),
                onNeutralClick
            )
        }
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        calendar.timeInMillis = bundle?.getLong(SavedTag.Date.VALUE) ?: defaultTime
        neutralVisible = bundle?.getBoolean(SavedTag.Date.VISIBLE) ?: DEF_NEUTRAL
        position = bundle?.getInt(SavedTag.Date.POSITION) ?: DEF_POSITION
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(SavedTag.Date.VALUE, calendar.timeInMillis)
        outState.putBoolean(SavedTag.Date.VISIBLE, neutralVisible)
        outState.putInt(SavedTag.Date.POSITION, position)
    }

    override fun setupButton() {
        super.setupButton()

        neutralButton?.visibility = if (neutralVisible) View.VISIBLE else View.GONE
    }

    override fun onTestUpdateDate(calendar: Calendar) {
        activity?.runOnUiThread {
            (dialog as? DatePickerDialog)?.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback = null
    }

    companion object {
        @RunNone var callback: DateTestCallback? = null

        private const val DEF_POSITION = -1
        private const val DEF_NEUTRAL = false
    }
}