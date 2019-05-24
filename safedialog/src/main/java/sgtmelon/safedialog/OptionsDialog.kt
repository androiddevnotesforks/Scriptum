package sgtmelon.safedialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.DialogBlank.Companion.INIT
import sgtmelon.safedialog.DialogBlank.Companion.VALUE

class OptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    lateinit var onClickListener: DialogInterface.OnClickListener

    private lateinit var items: List<String>

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

        items = savedInstanceState?.getStringArray(INIT)?.toList()
                ?: bundle?.getStringArray(INIT)?.toList()
                        ?: ArrayList()

        position = savedInstanceState?.getInt(VALUE) ?: bundle?.getInt(VALUE) ?: 0

        return AlertDialog.Builder(context as Activity)
                .setItems(items.toTypedArray(), this)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putStringArray(INIT, items.toTypedArray())
                putInt(VALUE, position)
            })

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        onClickListener.onClick(dialogInterface, i)
    }

}