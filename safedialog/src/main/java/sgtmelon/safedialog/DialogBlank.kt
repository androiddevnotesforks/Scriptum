package sgtmelon.safedialog

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.app.AlertDialog as AlertDialogOld

/**
 * Базовый класс диалогов для наследования
 */
abstract class DialogBlank : DialogFragment() {

    protected lateinit var activity: Activity

    lateinit var title: String
    lateinit var message: String

    protected var buttonPositive: Button? = null

    var positiveListener: DialogInterface.OnClickListener? = null
    protected val onPositiveClick = DialogInterface.OnClickListener { dialogInterface, i ->
        positiveListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

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
        const val POSITION = "POSITION"
        const val INIT = "INIT"
        const val VALUE = "VALUE"
    }

}