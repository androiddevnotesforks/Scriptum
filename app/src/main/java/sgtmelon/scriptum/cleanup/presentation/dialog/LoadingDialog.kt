package sgtmelon.scriptum.cleanup.presentation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.dialog.parent.BlankEmptyDialog
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.utils.applyTransparentBackground
import sgtmelon.scriptum.R
import sgtmelon.text.dotanim.DotAnimControl
import sgtmelon.text.dotanim.DotAnimType

/**
 * Dialog with endless progress bar and loading text.
 */
// TODO move into safedialogs
class LoadingDialog : BlankEmptyDialog(), DotAnimControl.Callback {

    private val loadingText get() = dialog?.findViewById<TextView>(R.id.loading_text)

    private val dotAnimControl = DotAnimControl(DotAnimType.SPAN, callback = this)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(requireContext())
            .setView(R.layout.view_loading)
            .setCancelable(false)
            .create()
            .applyTransparentBackground()
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