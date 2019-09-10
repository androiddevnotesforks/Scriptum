package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.addTextChangedListener
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.getColorAttr
import java.util.*

class RenameDialog : BlankDialog(), TextView.OnEditorActionListener {

    private val nameEnter get() = dialog?.findViewById<EditText?>(R.id.rename_enter)

    private var nameList: ArrayList<String> = ArrayList()
    var position = 0
        private set

    val name: String get() = nameEnter?.text?.toString()?.clearSpace() ?: ""

    fun setArguments(p: Int, title: String, nameList: List<String>) = apply {
        arguments = Bundle().apply {
            putInt(POSITION, p)
            putString(INIT, title)
            putStringArrayList(VALUE, ArrayList(nameList))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        position = savedInstanceState?.getInt(POSITION) ?: arguments?.getInt(POSITION) ?: 0
        title = savedInstanceState?.getString(INIT) ?: arguments?.getString(INIT) ?: ""

        nameList = savedInstanceState?.getStringArrayList(VALUE)
                ?: arguments?.getStringArrayList(VALUE) ?: ArrayList()

        return AlertDialog.Builder(context as Context)
                .setTitle(title)
                .setView(R.layout.view_rename)
                .setPositiveButton(getString(R.string.dialog_button_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(POSITION, position)
                putString(INIT, title)
                putStringArrayList(VALUE, nameList)
            })

    // TODO not show keyboard
    override fun setupView() {
        super.setupView()

        nameEnter?.apply {
            setTextColor(context.getColorAttr(R.attr.clContent))
            setHintTextColor(context.getColorAttr(R.attr.clDisable))

            setOnEditorActionListener(this@RenameDialog)
            addTextChangedListener(on = { setEnable() })

            requestFocus()
        }

        dialog?.window?.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
    }

    override fun setEnable() {
        super.setEnable()

        val isTitle = title.toUpperCase() == name.toUpperCase()

        positiveButton?.isEnabled = name.isNotEmpty() &&
                if (isTitle) title != name else !nameList.contains(name.toUpperCase())
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId != EditorInfo.IME_ACTION_DONE) return false

        if (name.isNotEmpty() && !nameList.contains(name.toUpperCase())) {
            positiveButton?.callOnClick()
            return true
        }

        return false
    }


    private fun String.toUpperCase() = toUpperCase(Locale.ROOT)

}