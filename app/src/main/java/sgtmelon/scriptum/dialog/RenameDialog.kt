package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.addTextChangedListener
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.getColorAttr
import java.util.*

class RenameDialog : DialogBlank(), TextView.OnEditorActionListener {

    var position = 0
        private set

    private var nameList: ArrayList<String> = arrayListOf()
    private var nameEnter: EditText? = null

    val name: String get() = nameEnter?.text?.toString()?.clearSpace() ?: ""

    fun setArguments(p: Int, title: String, nameList: ArrayList<String>) = apply {
        arguments = Bundle().apply {
            putInt(POSITION, p)
            putString(INIT, title)
            putStringArrayList(VALUE, nameList)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        position = savedInstanceState?.getInt(POSITION) ?: bundle?.getInt(POSITION) ?: 0
        title = savedInstanceState?.getString(INIT) ?: bundle?.getString(INIT) ?: ""

        nameList = savedInstanceState?.getStringArrayList(VALUE)
                ?: bundle?.getStringArrayList(VALUE) ?: ArrayList()

        val view = LayoutInflater.from(context).inflate(R.layout.view_rename, null)

        nameEnter = view.findViewById(R.id.rename_enter)
        nameEnter?.apply {
            setTextColor(context.getColorAttr(R.attr.clContent))
            setHintTextColor(context.getColorAttr(R.attr.clDisable))

            setOnEditorActionListener(this@RenameDialog)
            addTextChangedListener(on = { setEnable() })
        }

        return AlertDialog.Builder(context as Context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
                .apply {
                    nameEnter?.requestFocus()
                    window?.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
                }
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(POSITION, position)
                putString(INIT, title)
                putStringArrayList(VALUE, nameList)
            })

    override fun setEnable() {
        super.setEnable()
        positiveButton?.isEnabled = name.isNotEmpty() && !nameList.contains(name.toUpperCase())
    }

    override fun onEditorAction(textView: TextView, i: Int, keyEvent: KeyEvent): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        if (name.isNotEmpty() && !nameList.contains(name.toUpperCase())) {
            positiveButton?.callOnClick()
            return true
        }

        return false
    }

}