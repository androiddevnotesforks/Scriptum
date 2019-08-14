package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

/**
 * Dialog for single choice
 *
 * @author SerjantArbuz
 */
class SingleDialog : DialogBlank() {

    var itemArray: Array<String> = arrayOf()

    private var checkInit = 0
    var check = 0
        private set

    var itemListener: DialogInterface.OnClickListener? = null

    fun setArguments(check: Int) = apply {
        arguments = Bundle().apply {
            putInt(INIT, check)
            putInt(VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        checkInit = savedInstanceState?.getInt(INIT) ?: bundle?.getInt(INIT) ?: 0
        check = savedInstanceState?.getInt(VALUE) ?: bundle?.getInt(VALUE) ?: 0

        return AlertDialog.Builder(context as Context)
                .setTitle(title)
                .setSingleChoiceItems(itemArray, check) { _, i ->
                    itemListener?.onClick(dialog, i)
                    check = i
                    setEnable()
                }
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(INIT, checkInit)
                putInt(VALUE, check)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive?.isEnabled = checkInit != check
    }

}