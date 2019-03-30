package sgtmelon.safedialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import java.util.*

/**
 * Диалог с check-выбором пунктов
 *
 * @author SerjantArbuz
 * @version 1.0
 */
class MultiplyDialog : DialogBlank() {

    lateinit var name: List<String>

    private lateinit var init: BooleanArray

    lateinit var check: BooleanArray
        private set

    fun setArguments(checkArray: BooleanArray) {
        val bundle = Bundle()

        bundle.putBooleanArray(INIT, checkArray.clone())
        bundle.putBooleanArray(VALUE, checkArray)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getBooleanArray(INIT)
                ?: bundle?.getBooleanArray(INIT)
                ?: BooleanArray(size = 0)

        check = savedInstanceState?.getBooleanArray(VALUE)
                ?: bundle?.getBooleanArray(VALUE)
                ?: BooleanArray(size = 0)

        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setMultiChoiceItems(name.toTypedArray(), check) { _, which, isChecked ->
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

        outState.putBooleanArray(INIT, init)
        outState.putBooleanArray(VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()
        buttonPositive.isEnabled = !Arrays.equals(init, check)
    }

}