package sgtmelon.safedialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.DialogBlank.Companion.INIT
import sgtmelon.safedialog.DialogBlank.Companion.VALUE

/**
 * Dialog showing options with list item
 *
 * @author SerjantArbuz
 */
class OptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    var onClickListener: DialogInterface.OnClickListener? = null

    private var itemList: List<String> = ArrayList()

    /**
     * Save item position in list for next operations
     */
    var position: Int = 0
        private set

    fun setArguments(itemArray: Array<String>, p: Int) {
        arguments = Bundle().apply {
            putStringArray(INIT, itemArray)
            putInt(VALUE, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        itemList = savedInstanceState?.getStringArray(INIT)?.toList()
                ?: bundle?.getStringArray(INIT)?.toList() ?: ArrayList()

        position = savedInstanceState?.getInt(VALUE)
                ?: bundle?.getInt(VALUE) ?: 0

        return AlertDialog.Builder(context as Activity)
                .setItems(itemList.toTypedArray(), this)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putStringArray(INIT, itemList.toTypedArray())
                putInt(VALUE, position)
            })

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        onClickListener?.onClick(dialogInterface, i)
    }

}