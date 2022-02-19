package sgtmelon.safedialog.dialog.parent

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import android.app.AlertDialog as AlertDialogOld

/**
 * Base class for safe dialogs
 */
abstract class BlankDialog : DialogFragment() {

    var title: String = NdValue.TEXT
    var message: String = NdValue.TEXT

    protected var positiveButton: Button? = null
    protected var negativeButton: Button? = null
    protected var neutralButton: Button? = null

    var positiveListener: DialogInterface.OnClickListener? = null

    protected val onPositiveClick = DialogInterface.OnClickListener { dialogInterface, i ->
        positiveListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    var dismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            onRestoreContentState(savedInstanceState)
        }

        onRestoreInstanceState(bundle = savedInstanceState ?: arguments)

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SavedTag.TITLE, title)
        outState.putString(SavedTag.MESSAGE, message)
    }

    /**
     * Use for restore dialog content which was written before [safeShow]
     * (e.g. title, nameArray and ect.).
     *
     * Call inside [onCreateDialog] before create them.
     */
    @CallSuper
    open fun onRestoreContentState(savedInstanceState: Bundle) {
        title = savedInstanceState.getString(SavedTag.TITLE) ?: NdValue.TEXT
        message = savedInstanceState.getString(SavedTag.MESSAGE) ?: NdValue.TEXT
    }

    /**
     * Function for restore content which was passed throw setArgument functions
     * (e.g. position, check and ect.).
     */
    @CallSuper
    open fun onRestoreInstanceState(bundle: Bundle?) = Unit

    override fun onStart() {
        super.onStart()
        setupView()
        setupButton()
        setEnable()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }

    fun safeDismiss() {
        if (isAdded) dismiss()
    }

    /**
     * Func for setup child view's of custom view
     */
    @CallSuper protected open fun setupView() = Unit

    @CallSuper protected open fun setupButton() {
        val dialog = dialog

        positiveButton = when (dialog) {
            is AlertDialog -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            is AlertDialogOld -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            else -> null
        }

        negativeButton = when (dialog) {
            is AlertDialog -> dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            is AlertDialogOld -> dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            else -> null
        }

        neutralButton = when (dialog) {
            is AlertDialog -> dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
            is AlertDialogOld -> dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
            else -> null
        }
    }

    @CallSuper protected open fun setEnable() = Unit
}