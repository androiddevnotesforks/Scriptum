package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.annotation.SavedTag

/**
 * Dialog with message for user
 */
class MessageDialog : BlankDialog() {

    @MessageType var type: Int = MessageType.INFO

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(context as Context)
            .setTitle(title)
            .setMessage(message)

        if (type == MessageType.INFO) {
            builder.setPositiveButton(getString(R.string.dialog_button_ok), onPositiveClick)
        } else {
            builder.setPositiveButton(getString(R.string.dialog_button_yes), onPositiveClick)
            builder.setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ -> dialog.cancel() }
        }

        return builder.setCancelable(true).create().applyAnimation()
    }

    override fun onRestoreContentState(savedInstanceState: Bundle) {
        super.onRestoreContentState(savedInstanceState)
        type = savedInstanceState.getInt(SavedTag.INIT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SavedTag.INIT, type)
    }
}