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

    private lateinit var init: BooleanArray

    lateinit var check: BooleanArray
        private set

    fun setArguments(check: BooleanArray) {
        val bundle = Bundle()

        bundle.putBooleanArray(DialogAnn.INIT, check.clone())
        bundle.putBooleanArray(DialogAnn.VALUE, check)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getBooleanArray(DialogAnn.INIT)
                ?: bundle?.getBooleanArray(DialogAnn.INIT)
                ?: BooleanArray(size = 0)

        check = savedInstanceState?.getBooleanArray(DialogAnn.VALUE)
                ?: bundle?.getBooleanArray(DialogAnn.VALUE)
                ?: BooleanArray(size = 0)

        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setMultiChoiceItems(name, check) { _, which, isChecked ->
                    check[which] = isChecked
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
        buttonPositive.isEnabled = !Arrays.equals(init, check)
    }

}