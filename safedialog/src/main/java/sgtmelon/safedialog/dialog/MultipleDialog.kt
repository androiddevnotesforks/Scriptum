package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.utils.safeShow

/**
 * Dialog for multiply check choice
 */
class MultipleDialog : BlankDialog() {

    var itemArray: Array<String> = arrayOf()

    private var init = BooleanArray(size = 0)
    var check = BooleanArray(size = 0)
        private set

    var needOneSelect = NdValue.KEY

    /**
     * Call before [safeShow].
     */
    fun setArguments(checkArray: BooleanArray) = apply {
        arguments = Bundle().apply {
            putBooleanArray(SavedTag.INIT, checkArray.clone())
            putBooleanArray(SavedTag.VALUE, checkArray.clone())
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMultiChoiceItems(itemArray, check) { _, which, isChecked ->
                check[which] = isChecked
                changeButtonEnable()
            }
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)
        itemArray = savedState.getStringArray(SavedTag.LIST) ?: arrayOf()
        needOneSelect = savedState.getBoolean(SavedTag.KEY)
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)
        init = bundle?.getBooleanArray(SavedTag.INIT) ?: BooleanArray(size = 0)
        check = bundle?.getBooleanArray(SavedTag.VALUE) ?: BooleanArray(size = 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(SavedTag.LIST, itemArray)
        outState.putBoolean(SavedTag.KEY, needOneSelect)

        outState.putBooleanArray(SavedTag.INIT, init)
        outState.putBooleanArray(SavedTag.VALUE, check)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()
        positiveButton?.isEnabled =
                !init.contentEquals(check) && if (needOneSelect) check.contains(true) else true
    }

}