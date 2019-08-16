package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

/**
 * Dialog with message for user
 *
 * @author SerjantArbuz
 */
class MessageDialog : BlankDialog() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context as Context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.dialog_btn_yes), onPositiveClick)
                    .setNegativeButton(getString(R.string.dialog_btn_no)) { dialog, _ -> dialog.cancel() }
                    .setCancelable(true)
                    .create()

}