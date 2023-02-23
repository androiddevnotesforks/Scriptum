package sgtmelon.scriptum.cleanup.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.utils.showKeyboard
import sgtmelon.scriptum.R

/**
 * Dialog with EditText for rename category.
 */
class RenameDialog : BlankDialog(),
    TextView.OnEditorActionListener {

    private val nameEnter get() = dialog?.findViewById<EditText?>(R.id.rename_enter)

    private var nameList: ArrayList<String> = ArrayList()

    var position = DEF_POSITION
        private set

    val name: String get() = nameEnter?.text?.toString()?.removeExtraSpace() ?: ""

    fun setArguments(p: Int, title: String, nameList: List<String>) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.Common.POSITION, p)
            putString(SavedTag.Common.TITLE, title)
            putStringArrayList(SavedTag.Common.LIST, ArrayList(nameList))
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
            .showKeyboard()
            .applyAnimation()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        position = bundle?.getInt(SavedTag.Common.POSITION) ?: DEF_POSITION
        title = bundle?.getString(SavedTag.Common.TITLE) ?: DEF_TITLE
        nameList = bundle?.getStringArrayList(SavedTag.Common.LIST) ?: ArrayList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SavedTag.Common.POSITION, position)
        outState.putString(SavedTag.Common.TITLE, title)
        outState.putStringArrayList(SavedTag.Common.LIST, nameList)
    }

    override fun setupView() {
        super.setupView()

        nameEnter?.apply {
            setTextColor(context.getColorAttr(R.attr.clContent))
            setHintTextColor(context.getColorAttr(R.attr.clDisable))

            setOnEditorActionListener(this@RenameDialog)
            doOnTextChanged { _, _, _, _ -> changeButtonEnable() }

            requestFocus()
        }
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()

        positiveButton?.isEnabled = isSaveEnabled()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId != EditorInfo.IME_ACTION_DONE) return false

        if (isSaveEnabled()) {
            positiveButton?.callOnClick()
            return true
        }

        return false
    }

    private fun isSaveEnabled(): Boolean {
        if (name.isEmpty()) return false

        /**
         * If currently entered a title (ignore letter case) -> we may change name without
         * problem if it's not completely equals (without ignore letter case).
         */
        val isEqualTitle = title.uppercase() == name.uppercase()

        /**
         * If [name] not title -> check [nameList] for allowed name (if [nameList] not contains
         * our [name] -> may save).
         */
        return if (isEqualTitle) title != name else !nameList.contains(name.uppercase())
    }

    // TODO test for isSaveEnabled
    companion object {
        private const val DEF_POSITION = -1
        private const val DEF_TITLE = ""
    }
}