package sgtmelon.safedialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.BlankDialog.Companion.INIT
import sgtmelon.safedialog.BlankDialog.Companion.VALUE

/**
 * Dialog showing options with list item
 */
class OptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    var itemListener: DialogInterface.OnClickListener? = null

    var dismissListener: DialogInterface.OnDismissListener? = null

    private var itemList: List<String> = ArrayList()

    /**
     * Save item position in list for next operations
     */
    var position: Int = 0
        private set

    /**
     * Call before [show]
     */
    fun setArguments(itemArray: Array<String>, p: Int) = apply {
        arguments = Bundle().apply {
            putStringArray(INIT, itemArray)
            putInt(VALUE, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        itemList = savedInstanceState?.getStringArray(INIT)?.toList()
                ?: arguments?.getStringArray(INIT)?.toList() ?: ArrayList()

        position = savedInstanceState?.getInt(VALUE)
                ?: arguments?.getInt(VALUE) ?: 0

        return AlertDialog.Builder(context as Activity)
                .setItems(itemList.toTypedArray(), this)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putStringArray(INIT, itemList.toTypedArray())
            putInt(VALUE, position)
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        itemListener?.onClick(dialogInterface, i)
    }

}