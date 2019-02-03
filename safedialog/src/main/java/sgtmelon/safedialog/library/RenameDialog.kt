package sgtmelon.safedialog.library

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.R
import sgtmelon.safedialog.office.annot.DialogAnn
import sgtmelon.safedialog.office.blank.DialogBlank
import java.util.*

class RenameDialog : DialogBlank(), TextView.OnEditorActionListener {

    var position: Int = 0
        private set

    private var listName: ArrayList<String>? = null
    private var nameEnter: EditText? = null

    @ColorInt var colorText: Int = 0
    @ColorInt var colorHint: Int = 0

    var textHint: String = ""
    var textLength: Int = 0

    val name: String
        get() = nameEnter!!.text.toString()

    fun setArguments(p: Int, title: String, listName: ArrayList<String>) {
        val bundle = Bundle()

        bundle.putInt(DialogAnn.POSITION, p)
        bundle.putString(DialogAnn.INIT, title)
        bundle.putStringArrayList(DialogAnn.VALUE, listName)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(DialogAnn.POSITION)
            title = savedInstanceState.getString(DialogAnn.INIT) ?: ""
            listName = savedInstanceState.getStringArrayList(DialogAnn.VALUE)
        } else if (bundle != null) {
            position = bundle.getInt(DialogAnn.POSITION)
            title = bundle.getString(DialogAnn.INIT) ?: ""
            listName = bundle.getStringArrayList(DialogAnn.VALUE)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.view_rename, null)
        nameEnter = view.findViewById(R.id.rename_enter)

        nameEnter!!.setTextColor(colorText)
        nameEnter!!.setHintTextColor(colorHint)
        nameEnter!!.hint = textHint
        nameEnter!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(textLength))

        nameEnter!!.setOnEditorActionListener(this)
        nameEnter!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                setEnable()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(DialogAnn.POSITION, position)
        outState.putString(DialogAnn.INIT, title)
        outState.putStringArrayList(DialogAnn.VALUE, listName)
    }

    override fun setEnable() {
        super.setEnable()

        buttonPositive!!.isEnabled = !(TextUtils.isEmpty(name) || listName!!.contains(name.toUpperCase()))
    }

    override fun onEditorAction(textView: TextView, i: Int, keyEvent: KeyEvent): Boolean {
        if (i == EditorInfo.IME_ACTION_DONE) {
            if (!TextUtils.isEmpty(name) && !listName!!.contains(name.toUpperCase())) {
                buttonPositive!!.callOnClick()
                return true
            }
        }

        return false
    }

}