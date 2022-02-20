package sgtmelon.safedialog.dialog.parent

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import android.app.AlertDialog as AlertDialogOld

/**
 * Base class for safe dialogs
 */
abstract class BlankDialog : BlankEmptyDialog() {

    var title: String = DEF_TITLE

    protected var positiveButton: Button? = null
    protected var negativeButton: Button? = null
    protected var neutralButton: Button? = null

    var positiveListener: DialogInterface.OnClickListener? = null

    protected val onPositiveClick = DialogInterface.OnClickListener { dialogInterface, i ->
        positiveListener?.onClick(dialogInterface, i)
        dialogInterface.cancel()
    }

    protected val onNegativeClick = DialogInterface.OnClickListener { dialogInterface, i ->
        dialogInterface.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SavedTag.Blank.TITLE, title)
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        title = savedState.getString(SavedTag.Blank.TITLE) ?: DEF_TITLE
    }

    override fun onStart() {
        super.onStart()
        setupButton()
        changeButtonEnable()
    }

    @CallSuper
    protected open fun setupButton() {
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
     * Func for change [positiveButton]/[negativeButton]/[neutralButton] enable state.
     */
    @CallSuper
    protected open fun changeButtonEnable() = Unit

    companion object {
        private const val DEF_TITLE = ""
    }
}