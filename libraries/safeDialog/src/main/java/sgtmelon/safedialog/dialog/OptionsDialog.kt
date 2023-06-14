package sgtmelon.safedialog.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankEmptyDialog
import sgtmelon.safedialog.dialog.parent.create.CreateAlertDialog
import sgtmelon.safedialog.utils.safeShow

/**
 * Dialog showing options for choose (also work with list items - [position]).
 */
class OptionsDialog : BlankEmptyDialog(),
    CreateAlertDialog,
    DialogInterface.OnClickListener {

    @StyleRes override var themeId: Int? = null

    var title: String = DEF_TITLE
    var itemListener: DialogInterface.OnClickListener? = null

    inline fun onItem(crossinline func: (which: Int) -> Unit) {
        itemListener = DialogInterface.OnClickListener { _, which -> func(which) }
    }

    private var itemList: List<String> = emptyList()

    /**
     * Save note item position in list for next operations. [DEF_POSITION] - if dialog open
     * happened not from list (and probably not for notes).
     */
    var position: Int = DEF_POSITION
        private set

    /**
     * Call before [safeShow].
     *
     * [itemArray] - buttons for display
     * [p] - position of item in list
     */
    fun setArguments(itemArray: Array<String>, p: Int) = apply {
        arguments = Bundle().apply {
            putStringArray(SavedTag.Options.LIST, itemArray)
            putInt(SavedTag.Options.POSITION, p)
        }
    }

    override fun buildDialog(builder: AlertDialog.Builder, context: Context): AlertDialog.Builder {
        return builder.setItems(itemList.toTypedArray(), this)
            .setCancelable(true)
            .apply { title.takeIf { it != DEF_TITLE }?.let(::setTitle) }
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