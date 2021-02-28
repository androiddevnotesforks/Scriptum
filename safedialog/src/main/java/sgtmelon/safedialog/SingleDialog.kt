package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag

/**
 * Dialog for single choice
 */
class SingleDialog : BlankDialog() {

    var itemArray: Array<String> = arrayOf()

    /**
     * If value is TRUE - it means what user can choose any dialog item. Otherwise see [setEnable].
     */
    var applyEnable: Boolean = false

    private var checkInit = NdValue.CHECK
    var check = NdValue.CHECK
        private set

    var itemListener: DialogInterface.OnClickListener? = null

    /**
     * Call before [show].
     */
    fun setArguments(check: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.INIT, check)
            putInt(SavedTag.VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(context as Context)
            .setTitle(title)
            .setSingleChoiceItems(itemArray, check) { _, i ->
                itemListener?.onClick(dialog, i)
                check = i
                setEnable()
            }
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreContentState(savedInstanceState: Bundle) {
        super.onRestoreContentState(savedInstanceState)
        itemArray = savedInstanceState.getStringArray(SavedTag.LIST) ?: arrayOf()
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        super.onRestoreInstanceState(bundle)
        checkInit = bundle?.getInt(SavedTag.INIT) ?: NdValue.CHECK
        check = bundle?.getInt(SavedTag.VALUE) ?: NdValue.CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(SavedTag.LIST, itemArray)

        outState.putInt(SavedTag.INIT, checkInit)
        outState.putInt(SavedTag.VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()
        positiveButton?.isEnabled = applyEnable || checkInit != check
    }
}