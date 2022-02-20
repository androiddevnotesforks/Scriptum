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
import sgtmelon.safedialog.utils.safeShow

/**
 * Base class for safe dialogs
 */
abstract class BlankDialog : BlankEmptyDialog() {

    var title: String = NdValue.TEXT

    // TODO move into message dialog
    var message: String = NdValue.TEXT

    protected var positiveButton: Button? = null
    protected var negativeButton: Button? = null
    protected var neutralButton: Button? = null

    var positiveListener: DialogInterface.OnClickListener? = null

    protected val onPositiveClick = DialogInterface.OnClickListener { dialogInterface, i ->
        positiveListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            onRestoreContentState(savedInstanceState)
        }

        onRestoreArgumentState(bundle = savedInstanceState ?: arguments)

        return super.onCreateDialog(savedInstanceState)
    }

    //region Save/Restore functions

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SavedTag.TITLE, title)
        outState.putString(SavedTag.MESSAGE, message)
    }

    override fun onRestoreContentState(savedInstanceState: Bundle) {
        super.onRestoreContentState(savedInstanceState)

        title = savedInstanceState.getString(SavedTag.TITLE) ?: NdValue.TEXT
        message = savedInstanceState.getString(SavedTag.MESSAGE) ?: NdValue.TEXT
    }

    /**
     * Function for restore content which was passed through setArgument function
     * (e.g. position, check and ect.).
     */
    @CallSuper
    protected open fun onRestoreArgumentState(bundle: Bundle?) = Unit

    //endregion

    override fun onStart() {
        super.onStart()
        setupButton()
        changeButtonEnable()
    }

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

    /**
     * Function for change [positiveButton]/[negativeButton]/[neutralButton] enable state.
     */
    @CallSuper protected open fun changeButtonEnable() = Unit

}