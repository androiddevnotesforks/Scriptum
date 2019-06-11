package sgtmelon.safedialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import java.util.*

/**
 * Диалог с check-выбором пунктов
 *
 * @author SerjantArbuz
 */
class MultiplyDialog : DialogBlank() {

    lateinit var name: List<String>
    var needOneSelect = false

    private lateinit var init: BooleanArray

    lateinit var check: BooleanArray
        private set

    fun setArguments(checkArray: BooleanArray) {
        arguments = Bundle().apply {
            putBooleanArray(INIT, checkArray.clone())
            putBooleanArray(VALUE, checkArray)
        }
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

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putBooleanArray(INIT, init)
                putBooleanArray(VALUE, check)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive?.isEnabled = !Arrays.equals(init, check) &&
                if (needOneSelect) check.contains(true) else true
    }

}