package sgtmelon.safedialog.library

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.office.annot.DialogAnn
import sgtmelon.safedialog.office.blank.DialogBlank

class SingleDialog : DialogBlank() {

    lateinit var rows: Array<String>

    private var init: Int = 0
    var check: Int = 0
        private set

    fun setArguments(check: Int) {
        val bundle = Bundle()

        bundle.putInt(DialogAnn.INIT, check)
        bundle.putInt(DialogAnn.VALUE, check)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        if (savedInstanceState != null) {
            init = savedInstanceState.getInt(DialogAnn.INIT)
            check = savedInstanceState.getInt(DialogAnn.VALUE)
        } else if (bundle != null) {
            init = bundle.getInt(DialogAnn.INIT)
            check = bundle.getInt(DialogAnn.VALUE)
        }

        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setSingleChoiceItems(rows, check) { _, i ->
                    check = i
                    setEnable()
                }
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(DialogAnn.INIT, init)
        outState.putInt(DialogAnn.VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()

        buttonPositive!!.isEnabled = init != check
    }

}