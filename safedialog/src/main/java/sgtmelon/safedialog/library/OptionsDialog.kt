package sgtmelon.safedialog.library

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.office.annot.DialogAnn

class OptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    lateinit var onClickListener: DialogInterface.OnClickListener

    private lateinit var items: List<String>

    var position: Int = 0
        private set

    fun setArguments(items: Array<String>, p: Int) {
        val bundle = Bundle()

        bundle.putStringArray(DialogAnn.INIT, items)
        bundle.putInt(DialogAnn.VALUE, p)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        items = savedInstanceState?.getStringArray(DialogAnn.INIT)?.toList()
                ?: bundle?.getStringArray(DialogAnn.INIT)?.toList()
                ?: ArrayList()

        position = savedInstanceState?.getInt(DialogAnn.VALUE)
                ?: bundle?.getInt(DialogAnn.VALUE)
                ?: 0

        return AlertDialog.Builder(context!!)
                .setItems(items.toTypedArray(), this)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArray(DialogAnn.INIT, items.toTypedArray())
        outState.putInt(DialogAnn.VALUE, position)
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        onClickListener.onClick(dialogInterface, i)
    }

}