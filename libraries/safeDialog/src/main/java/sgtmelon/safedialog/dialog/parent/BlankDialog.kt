package sgtmelon.safedialog.dialog.parent

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
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

    //region Buttons listeners setup

    private var positiveListener: DialogInterface.OnClickListener? = null
    private var negativeListener: DialogInterface.OnClickListener? = null
    private var neutralListener: DialogInterface.OnClickListener? = null

    fun onPositiveClick(func: (dialog: DialogInterface) -> Unit) {
        positiveListener = DialogInterface.OnClickListener { dialog, _ -> func(dialog) }
    }

    fun onNegativeClick(func: (dialog: DialogInterface) -> Unit) {
        negativeListener = DialogInterface.OnClickListener { dialog, _ -> func(dialog) }
    }

    fun onNeutralClick(func: () -> Unit) {
        neutralListener = DialogInterface.OnClickListener { _, _ -> func() }
    }

    protected val onPositiveClick = DialogInterface.OnClickListener { dialog, i ->
        positiveListener?.onClick(dialog, i)
        dialog.cancel()
    }

    protected val onNegativeClick = DialogInterface.OnClickListener { dialog, i ->
        negativeListener?.onClick(dialog, i)
        dialog.cancel()
    }

    protected val onNeutralClick = DialogInterface.OnClickListener { dialog, i ->
        neutralListener?.onClick(dialog, i)
        dialog.cancel()
    }

    //endregion

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        title = savedState.getString(SavedTag.Blank.TITLE) ?: DEF_TITLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SavedTag.Blank.TITLE, title)
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