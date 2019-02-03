package sgtmelon.safedialog.library

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.office.blank.DialogBlank

class MessageDialog : DialogBlank() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_btn_yes), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_no)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

}
