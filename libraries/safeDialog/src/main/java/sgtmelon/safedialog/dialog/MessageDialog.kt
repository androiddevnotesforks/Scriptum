package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.extensions.decode
import sgtmelon.extensions.encode
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

        builder.setPositiveButton(getString(type.positiveButton), onPositiveClick)

        type.negativeButton?.let {
            builder.setNegativeButton(getString(it), onNegativeClick)
        }
        type.neutralButton?.let {
            builder.setNeutralButton(getString(it), onNeutralClick)
        }

        return builder.setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SavedTag.Message.TYPE, type.encode())
        outState.putString(SavedTag.Message.TEXT, message)
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        type = savedState.getString(SavedTag.Message.TYPE)?.decode() as? MessageType ?: DEF_TYPE
        message = savedState.getString(SavedTag.Message.TEXT) ?: DEF_TEXT
    }

    companion object {
        private val DEF_TYPE = MessageType.Info
        private const val DEF_TEXT = ""
    }
}