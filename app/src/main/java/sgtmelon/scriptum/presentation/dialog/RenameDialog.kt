package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.addTextChangedListener
import sgtmelon.scriptum.extension.clearSpace
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.extension.toUpperCase
import java.util.*

class RenameDialog : BlankDialog(), TextView.OnEditorActionListener {

    private val nameEnter get() = dialog?.findViewById<EditText?>(R.id.rename_enter)

    private var nameList: ArrayList<String> = ArrayList()
    var position = NdValue.POSITION
        private set

    val name: String get() = nameEnter?.text?.toString()?.clearSpace() ?: ""

    fun setArguments(p: Int, title: String, nameList: List<String>) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.POSITION, p)
            putString(SavedTag.INIT, title)
            putStringArrayList(SavedTag.VALUE, ArrayList(nameList))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(R.layout.view_rename)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
            .apply { window?.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE) }
            .applyAnimation()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)
        position = bundle?.getInt(SavedTag.POSITION) ?: NdValue.POSITION
        title = bundle?.getString(SavedTag.INIT) ?: NdValue.TEXT
        nameList = bundle?.getStringArrayList(SavedTag.VALUE) ?: ArrayList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SavedTag.POSITION, position)
        outState.putString(SavedTag.INIT, title)
        outState.putStringArrayList(SavedTag.VALUE, nameList)
    }

    override fun setupView() {
        super.setupView()

        nameEnter?.apply {
            setTextColor(context.getColorAttr(R.attr.clContent))
            setHintTextColor(context.getColorAttr(R.attr.clDisable))

            setOnEditorActionListener(this@RenameDialog)
            addTextChangedListener(on = { changeButtonEnable() })

            requestFocus()
        }
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()

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
}