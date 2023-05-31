package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankButtonDialog
import sgtmelon.safedialog.utils.safeShow

/**
 * Dialog for multiply check choice
 */
class MultipleDialog : BlankButtonDialog() {

    var itemArray: Array<String> = emptyArray()

    private var checkInit = BooleanArray(size = 0)

    /**
     * This is a result check positions.
     */
    var check = BooleanArray(size = 0)
        private set

    /**
     * If true - [check] should contain at least one true value for success apply.
     */
    var atLeastOne = DEF_AT_LEAST

    /**
     * Call before [safeShow].
     *
     * [checkArray] - position of selected item inside [itemArray].
     */
    fun setArguments(checkArray: BooleanArray) = apply {
        arguments = Bundle().apply {
            putBooleanArray(SavedTag.Multiple.CHECK_INIT, checkArray.clone())
            putBooleanArray(SavedTag.Multiple.CHECK, checkArray.clone())
        }
    }


    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMultiChoiceItems(itemArray, check) { _, which, isChecked ->
                check[which] = isChecked
                changeButtonEnable()
            }
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        itemArray = savedState.getStringArray(SavedTag.Multiple.LIST) ?: emptyArray()
        atLeastOne = savedState.getBoolean(SavedTag.Multiple.AT_LEAST, DEF_AT_LEAST)
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        checkInit = bundle?.getBooleanArray(SavedTag.Multiple.CHECK_INIT) ?: BooleanArray(size = 0)
        check = bundle?.getBooleanArray(SavedTag.Multiple.CHECK) ?: BooleanArray(size = 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArray(SavedTag.Multiple.LIST, itemArray)
        outState.putBoolean(SavedTag.Multiple.AT_LEAST, atLeastOne)

        outState.putBooleanArray(SavedTag.Multiple.CHECK_INIT, checkInit)
        outState.putBooleanArray(SavedTag.Multiple.CHECK, check)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()

        val atLeastEnabled = if (atLeastOne) check.contains(true) else true
        positiveButton?.isEnabled = !checkInit.contentEquals(check) && atLeastEnabled
    }

    companion object {
        private const val DEF_AT_LEAST = false
    }
}