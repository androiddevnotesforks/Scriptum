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
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimation

/**
 * Dialog with endless progress bar and loading text.
 */
// TODO move into safedialogs
class LoadingDialog : BlankEmptyDialog(), DotAnimation.Callback {

    private val loadingText get() = dialog?.findViewById<TextView>(R.id.loading_text)

    private val dotAnimation = DotAnimation(DotAnimType.SPAN, callback = this)

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

        dotAnimation.start(context, R.string.dialog_text_loading)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dotAnimation.stop()
    }

    override fun onDotAnimationUpdate(text: CharSequence) {
        loadingText?.text = text
    }
}