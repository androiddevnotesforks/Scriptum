package sgtmelon.safedialog.library

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.office.annot.DialogAnn

class OptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    lateinit var onClickListener: DialogInterface.OnClickListener

    private var items: Array<String>? = null

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

        if (savedInstanceState != null) {
            items = savedInstanceState.getStringArray(DialogAnn.INIT)
            position = savedInstanceState.getInt(DialogAnn.VALUE)
        } else if (bundle != null) {
            items = bundle.getStringArray(DialogAnn.INIT)
            position = bundle.getInt(DialogAnn.VALUE)
        }

        return AlertDialog.Builder(context!!)
                .setItems(items, this)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArray(DialogAnn.INIT, items)
        outState.putInt(DialogAnn.VALUE, position)
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        onClickListener.onClick(dialogInterface, i)
    }

}