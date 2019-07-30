package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import java.util.*
import kotlin.collections.ArrayList

/**
 * Dialog for multiply check choice
 *
 * @author SerjantArbuz
 */
class MultiplyDialog : DialogBlank() {

    var itemList: List<String> = ArrayList()

    private var init = BooleanArray(size = 0)
    var check = BooleanArray(size = 0)
        private set

    var needOneSelect = false

    fun setArguments(checkArray: BooleanArray): MultiplyDialog {
        arguments = Bundle().apply {
            putBooleanArray(INIT, checkArray.clone())
            putBooleanArray(VALUE, checkArray.clone())
        }

        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getBooleanArray(INIT)
                ?: bundle?.getBooleanArray(INIT) ?: BooleanArray(size = 0)

        check = savedInstanceState?.getBooleanArray(VALUE)
                ?: bundle?.getBooleanArray(VALUE) ?: BooleanArray(size = 0)

        return AlertDialog.Builder(context as Context)
                .setTitle(title)
                .setMultiChoiceItems(itemList.toTypedArray(), check) { _, which, isChecked ->
                    check[which] = isChecked
                    setEnable()
                }
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putBooleanArray(INIT, init)
                putBooleanArray(VALUE, check)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive?.isEnabled =
                !Arrays.equals(init, check) && if (needOneSelect) check.contains(true) else true
    }

}