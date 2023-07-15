package sgtmelon.scriptum.infrastructure.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.dialog.parent.BlankEmptyDialog
import sgtmelon.safedialog.utils.applyTransparentBackground
import sgtmelon.scriptum.R
import sgtmelon.textDotAnim.DotAnimType
import sgtmelon.textDotAnim.DotAnimationImpl
import sgtmelon.textDotAnim.DotText

/**
 * Dialog with endless progress bar and loading text.
 */
class LoadingDialog : BlankEmptyDialog(),
    DotAnimationImpl.Callback {

    private val loadingText get() = dialog?.findViewById<TextView>(R.id.loading_text)

    private val dotAnimation = DotAnimationImpl[lifecycle, DotAnimType.SPAN, this]

    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setView(R.layout.view_loading)
            .setCancelable(false)
            .create()
    }

    override fun transformDialog(dialog: Dialog): Dialog {
        return super.transformDialog(dialog)
            .applyTransparentBackground()
    }

    override fun setupView() {
        super.setupView()

        dotAnimation.start(context, R.string.dialog_text_loading)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dotAnimation.stop()
    }

    override fun onDotAnimationUpdate(text: DotText) {
        loadingText?.text = text.value
    }
}