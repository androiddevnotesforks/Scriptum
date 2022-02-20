package sgtmelon.safedialog.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankEmptyDialog
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.safedialog.utils.safeShow

/**
 * Dialog showing options for choose (also work with list items - [position]).
 */
class OptionsDialog : BlankEmptyDialog(),
    DialogInterface.OnClickListener {

    var title: String = DEF_TITLE
    var itemListener: DialogInterface.OnClickListener? = null

    private var itemList: List<String> = emptyList()

    /**
     * Save note item position in list for next operations. [DEF_POSITION] - if dialog open
     * happened not from list (and probably not for notes).
     */
    var position: Int = DEF_POSITION
        private set

    /**
     * Call before [safeShow].
     */
    fun setArguments(itemArray: Array<String>, p: Int) = apply {
        arguments = Bundle().apply {
            putStringArray(SavedTag.Options.LIST, itemArray)
            putInt(SavedTag.Options.POSITION, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(requireContext())
            .setItems(itemList.toTypedArray(), this)
            .setCancelable(true)

        if (title != DEF_TITLE) {
            builder.setTitle(title)
        }

        return builder.create().applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SavedTag.Options.TITLE, title)
        outState.putStringArray(SavedTag.Options.LIST, itemList.toTypedArray())
        outState.putInt(SavedTag.Options.POSITION, position)
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        title = savedState.getString(SavedTag.Options.TITLE) ?: DEF_TITLE
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        itemList = bundle?.getStringArray(SavedTag.Options.LIST)?.toList() ?: emptyList()
        position = bundle?.getInt(SavedTag.Options.POSITION) ?: DEF_POSITION
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        itemListener?.onClick(dialogInterface, i)
    }

    companion object {
        private const val DEF_TITLE = ""
        private const val DEF_POSITION = -1
    }
}