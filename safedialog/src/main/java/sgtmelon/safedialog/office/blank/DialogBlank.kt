package sgtmelon.safedialog.office.blank

import android.content.DialogInterface
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**
 * Базовый класс диалогов для наследования
 */
open class DialogBlank : DialogFragment() {

    lateinit var title: String
    lateinit var message: String

    protected var buttonPositive: Button? = null
    protected var buttonNeutral: Button? = null

    var positiveListener: DialogInterface.OnClickListener? = null
    protected val onPositiveClick = DialogInterface.OnClickListener { dialogInterface, i ->
        positiveListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    var neutralListener: DialogInterface.OnClickListener? = null
    protected val onNeutralClick = DialogInterface.OnClickListener { dialogInterface, i ->
        neutralListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onStart() {
        super.onStart()
        setEnable()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)

        dismissListener?.onDismiss(dialog)
    }

    @CallSuper
    protected open fun setEnable() {
        val dialog = dialog as AlertDialog

        buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonNeutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
    }

}