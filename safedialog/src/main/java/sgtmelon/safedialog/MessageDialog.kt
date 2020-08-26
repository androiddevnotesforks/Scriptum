package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

/**
 * Dialog with message for user
 */
class MessageDialog : BlankDialog() {

    @MessageType var type: Int = MessageType.INFO

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context as Context)
            .setTitle(title)
            .setMessage(message)
            .apply {
                if (type == MessageType.INFO) {
                    setPositiveButton(getString(R.string.dialog_button_ok), onPositiveClick)
                } else {
                    setPositiveButton(getString(R.string.dialog_button_yes), onPositiveClick)
                    setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ -> dialog.cancel() }
                }
            }
            .setCancelable(true)
            .create()
            .applyAnimation()
    }
}