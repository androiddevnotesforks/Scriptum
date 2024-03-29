package sgtmelon.scriptum.infrastructure.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import sgtmelon.extensions.emptyString
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.removeExtraSpace
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankButtonDialog
import sgtmelon.safedialog.utils.showKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.selectAllText
import sgtmelon.scriptum.infrastructure.utils.extensions.setSelectionSafe

/**
 * Dialog with [EditText] for rename category.
 */
class RenameDialog : BlankButtonDialog(),
    TextView.OnEditorActionListener {

    private val nameEnter: EditText? get() = dialog?.findViewById(R.id.rename_enter)

    private var nameList: ArrayList<String> = ArrayList()

    var position = DEF_POSITION
        private set

    val name: String get() = nameEnter?.text?.toString()?.removeExtraSpace() ?: emptyString()

    fun setArguments(p: Int, title: String, nameList: List<String>) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.Common.POSITION, p)
            putString(SavedTag.Common.TITLE, title)
            putStringArrayList(SavedTag.Common.LIST, ArrayList(nameList))
        }
    }

    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setView(R.layout.view_rename)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
    }

    override fun transformDialog(dialog: Dialog): Dialog {
        return super.transformDialog(dialog)
            .showKeyboard()
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
            setHintTextColor(context.getColorAttr(R.attr.clContentThird))

            setOnEditorActionListener(this@RenameDialog)
            doOnTextChanged { _, _, _, _ -> changeButtonEnable() }

            if (!isContentRestored) {
                /**
                 * If first open (not after rotation) -> set text and select all. Don't need to
                 * set text in other cases, because [EditText] handle rotation and save state
                 * itself (text in particular).
                 */
                setText(title)
                selectAllText()
            } else if (text.toString() == title) {
                /** If the same text (as default) -> select all. */
                selectAllText()
            } else {
                /** In other cases set cursor to the end. */
                setSelectionSafe()
            }
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