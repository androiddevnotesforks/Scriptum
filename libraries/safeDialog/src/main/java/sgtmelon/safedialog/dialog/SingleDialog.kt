package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.utils.safeShow

/**
 * Dialog for single choice.
 */
class SingleDialog : BlankDialog() {

    var itemArray: Array<String> = emptyArray()

    /**
     * If value is TRUE - it means what user can choose any dialog item (even previous).
     * Otherwise see [changeButtonEnable].
     */
    var applyEnable: Boolean = DEF_APPLY

    private var checkInit = DEF_CHECK

    /**
     * This is a result check position.
     */
    var check = DEF_CHECK
        private set

    var itemListener: DialogInterface.OnClickListener? = null

    inline fun onItemClick(crossinline func: (i: Int) -> Unit) {
        itemListener = DialogInterface.OnClickListener { _, i -> func(i) }
    }

    /**
     * Call before [safeShow].
     *
     * [check] - position of selected item inside [itemArray].
     */
    fun setArguments(check: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.Single.CHECK_INIT, check)
            putInt(SavedTag.Single.CHECK, check)
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
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        itemArray = savedState.getStringArray(SavedTag.Single.LIST) ?: emptyArray()
        applyEnable = savedState.getBoolean(SavedTag.Single.APPLY, DEF_APPLY)
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        checkInit = bundle?.getInt(SavedTag.Single.CHECK_INIT) ?: DEF_CHECK
        check = bundle?.getInt(SavedTag.Single.CHECK) ?: DEF_CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArray(SavedTag.Single.LIST, itemArray)
        outState.putBoolean(SavedTag.Single.APPLY, applyEnable)

        outState.putInt(SavedTag.Single.CHECK_INIT, checkInit)
        outState.putInt(SavedTag.Single.CHECK, check)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()

        positiveButton?.isEnabled = applyEnable || checkInit != check
    }

    companion object {
        private const val DEF_CHECK = 0
        private const val DEF_APPLY = false
    }
}