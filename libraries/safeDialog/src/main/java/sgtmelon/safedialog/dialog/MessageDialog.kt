package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.extensions.decode
import sgtmelon.extensions.encode
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankButtonDialog

/**
 * Dialog with title and message for user.
 */
class MessageDialog : BlankButtonDialog() {

    var type: MessageType = DEF_TYPE
    var message: String = DEF_TEXT

    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(type.positiveButton), onPositiveClick)
            .apply {
                type.negativeButton?.let { setNegativeButton(getString(it), onNegativeClick) }
                type.neutralButton?.let { setNeutralButton(getString(it), onNeutralClick) }
            }
            .setCancelable(true)
            .create()
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