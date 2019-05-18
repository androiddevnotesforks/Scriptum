package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.utils.ColorUtils.getColorAttr
import sgtmelon.scriptum.office.utils.addTextChangedListener
import sgtmelon.scriptum.office.utils.getClearText
import java.util.*

class RenameDialog : DialogBlank(), TextView.OnEditorActionListener {

    var position: Int = 0
        private set

    private lateinit var nameList: ArrayList<String>
    private lateinit var nameEnter: EditText

    val name: String get() = nameEnter.getClearText()

    fun setArguments(p: Int, title: String, nameList: ArrayList<String>) {
        val bundle = Bundle()

        bundle.putInt(POSITION, p)
        bundle.putString(INIT, title)
        bundle.putStringArrayList(VALUE, nameList)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        position = savedInstanceState?.getInt(POSITION) ?: bundle?.getInt(POSITION) ?: 0
        title = savedInstanceState?.getString(INIT) ?: bundle?.getString(INIT) ?: ""

        nameList = savedInstanceState?.getStringArrayList(VALUE)
                ?: bundle?.getStringArrayList(VALUE) ?: ArrayList()

        val view = LayoutInflater.from(context).inflate(R.layout.view_rename, null)

        nameEnter = view.findViewById(R.id.rename_enter)
        nameEnter.apply {
            setTextColor(activity.getColorAttr(R.attr.clContent))
            setHintTextColor(activity.getColorAttr(R.attr.clDisable))

            setOnEditorActionListener(this@RenameDialog)
            addTextChangedListener(on = { setEnable() })
        }

        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(POSITION, position)
                putString(INIT, title)
                putStringArrayList(VALUE, nameList)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive.isEnabled = name.isNotEmpty() && !nameList.contains(name.toUpperCase())
    }

    override fun onEditorAction(textView: TextView, i: Int, keyEvent: KeyEvent): Boolean {
        if (i != EditorInfo.IME_ACTION_DONE) return false

        if (name.isNotEmpty() && !nameList.contains(name.toUpperCase())) {
            buttonPositive.callOnClick()
            return true
        }

        return false
    }

}