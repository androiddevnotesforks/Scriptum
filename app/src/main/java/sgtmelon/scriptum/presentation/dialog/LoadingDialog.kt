package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.safedialog.applyAnimation
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.key.DotAnimType
import sgtmelon.scriptum.presentation.control.DotAnimControl

/**
 * Dialog with endless progress bar.
 */
class LoadingDialog : BlankDialog(),
    DotAnimControl.Callback {

    private val loadingText get() = dialog?.findViewById<TextView?>(R.id.loading_text)

    private val dotAnimControl = DotAnimControl(DotAnimType.SPAN, callback = this)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(context as Context)
            .setView(R.layout.view_loading)
            .setCancelable(false)
            .create()
            .applyAnimation()
    }

    override fun setupView() {
        super.setupView()

        val context = context
        if (context != null) {
            dotAnimControl.start(context, R.string.dialog_text_loading)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dotAnimControl.stop()
    }

    override fun onDotAnimUpdate(text: CharSequence) {
        loadingText?.text = text
    }
}