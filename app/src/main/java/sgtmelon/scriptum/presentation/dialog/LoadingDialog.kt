package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.scriptum.R

/**
 * Dialog with endless progress bar.
 */
class LoadingDialog : BlankDialog() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context as Context)
                .setView(R.layout.view_loading)
                .setCancelable(false)
                .create()
    }

}