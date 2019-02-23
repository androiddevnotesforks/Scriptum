package sgtmelon.safedialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

class MessageDialog : DialogBlank() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_btn_yes), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_no)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

}
