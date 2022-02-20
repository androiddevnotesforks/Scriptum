package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.dialog.parent.BlankDialog

/**
 * Dialog with title and message for user.
 */
class MessageDialog : BlankDialog() {

    var type: MessageType = DEF_TYPE
    var message: String = DEF_TEXT

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)

        if (type == MessageType.INFO) {
            builder.setPositiveButton(getString(R.string.dialog_button_ok), onPositiveClick)
        } else {
            builder.setPositiveButton(getString(R.string.dialog_button_yes), onPositiveClick)
            builder.setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ -> dialog.cancel() }
        }

        return builder.setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SavedTag.Message.TYPE, type.ordinal)
        outState.putString(SavedTag.Message.TEXT, message)
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        val ordinal = savedState.getInt(SavedTag.Message.TYPE)
        type = MessageType.values().getOrNull(ordinal) ?: DEF_TYPE
        message = savedState.getString(SavedTag.Message.TEXT) ?: DEF_TEXT
    }

    companion object {
        private val DEF_TYPE = MessageType.INFO
        private const val DEF_TEXT = ""
    }
}