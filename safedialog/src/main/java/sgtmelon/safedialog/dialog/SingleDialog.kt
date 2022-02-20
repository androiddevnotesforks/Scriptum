package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.utils.safeShow

/**
 * Dialog for single choice
 */
class SingleDialog : BlankDialog() {

    var itemArray: Array<String> = arrayOf()

    /**
     * If value is TRUE - it means what user can choose any dialog item. Otherwise see [changeButtonEnable].
     */
    var applyEnable: Boolean = false

    private var checkInit = NdValue.CHECK
    var check = NdValue.CHECK
        private set

    var itemListener: DialogInterface.OnClickListener? = null

    /**
     * Call before [safeShow].
     */
    fun setArguments(check: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.INIT, check)
            putInt(SavedTag.VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setSingleChoiceItems(itemArray, check) { _, i ->
                itemListener?.onClick(dialog, i)
                check = i
                changeButtonEnable()
            }
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)
        itemArray = savedState.getStringArray(SavedTag.LIST) ?: arrayOf()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)
        checkInit = bundle?.getInt(SavedTag.INIT) ?: NdValue.CHECK
        check = bundle?.getInt(SavedTag.VALUE) ?: NdValue.CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(SavedTag.LIST, itemArray)

        outState.putInt(SavedTag.INIT, checkInit)
        outState.putInt(SavedTag.VALUE, check)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()
        positiveButton?.isEnabled = applyEnable || checkInit != check
    }
}