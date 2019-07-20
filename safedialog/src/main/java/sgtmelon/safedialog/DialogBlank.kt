package sgtmelon.safedialog

import android.content.DialogInterface
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.app.AlertDialog as AlertDialogOld

/**
 * Base class for safe dialogs
 *
 * @author SerjantArbuz
 */
abstract class DialogBlank : DialogFragment() {

    var title: String = ""
    var message: String = ""

    protected var buttonPositive: Button? = null

    var positiveListener: DialogInterface.OnClickListener? = null

    protected val onPositiveClick = DialogInterface.OnClickListener { dialogInterface, i ->
        positiveListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onStart() {
        super.onStart()
        setEnable()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }

    @CallSuper protected open fun setEnable() {
        buttonPositive = when (val dialog = dialog) {
            is AlertDialog -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            is AlertDialogOld -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            else -> null
        }
    }

    companion object {
        private const val PREFIX = "SAFE_DIALOG"

        const val POSITION = "${PREFIX}_POSITION"
        const val INIT = "${PREFIX}_INIT"
        const val VALUE = "${PREFIX}_VALUE"
    }

}