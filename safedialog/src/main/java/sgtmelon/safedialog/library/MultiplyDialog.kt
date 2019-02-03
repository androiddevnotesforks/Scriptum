package sgtmelon.safedialog.library

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.office.annot.DialogAnn
import sgtmelon.safedialog.office.blank.DialogBlank
import java.util.*

class MultiplyDialog : DialogBlank() {

    lateinit var name: Array<String>

    private var init: BooleanArray? = null

    var check: BooleanArray? = null
        private set

    fun setArguments(check: BooleanArray) {
        val bundle = Bundle()

        bundle.putBooleanArray(DialogAnn.INIT, check.clone())
        bundle.putBooleanArray(DialogAnn.VALUE, check)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        if (savedInstanceState != null) {
            init = savedInstanceState.getBooleanArray(DialogAnn.INIT)
            check = savedInstanceState.getBooleanArray(DialogAnn.VALUE)
        } else if (bundle != null) {
            init = bundle.getBooleanArray(DialogAnn.INIT)
            check = bundle.getBooleanArray(DialogAnn.VALUE)
        }

        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setMultiChoiceItems(name, check) { _, which, isChecked ->
                    check?.set(which, isChecked)
                    setEnable()
                }
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBooleanArray(DialogAnn.INIT, init)
        outState.putBooleanArray(DialogAnn.VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()

        buttonPositive!!.isEnabled = !Arrays.equals(init, check)
    }

}