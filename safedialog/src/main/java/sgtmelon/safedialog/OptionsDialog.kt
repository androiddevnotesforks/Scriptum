package sgtmelon.safedialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag

/**
 * Dialog showing options with list item
 */
class OptionsDialog : DialogFragment(), DialogInterface.OnClickListener {

    var title: String = NdValue.TEXT

    var itemListener: DialogInterface.OnClickListener? = null
    var dismissListener: DialogInterface.OnDismissListener? = null

    private var itemList: List<String> = ArrayList()

    /**
     * Save item position in list for next operations
     */
    var position: Int = NdValue.POSITION
        private set

    /**
     * Call before [safeShow].
     */
    fun setArguments(itemArray: Array<String>, p: Int) = apply {
        arguments = Bundle().apply {
            putStringArray(SavedTag.INIT, itemArray)
            putInt(SavedTag.VALUE, p)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            onRestoreContentState(savedInstanceState)
        }

        onRestoreInstanceState(bundle = savedInstanceState ?: arguments)

        val builder = AlertDialog.Builder(context as Context)
            .setItems(itemList.toTypedArray(), this)
            .setCancelable(true)

        if (title != NdValue.TEXT) {
            builder.setTitle(title)
        }

        return builder.create().applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SavedTag.TITLE, title)
        outState.putStringArray(SavedTag.INIT, itemList.toTypedArray())
        outState.putInt(SavedTag.VALUE, position)
    }

    /**
     * Use for restore dialog content which was written before [safeShow]
     * (e.g. title, nameArray and ect.).
     *
     * Call inside [onCreateDialog] before create them.
     */
    private fun onRestoreContentState(savedInstanceState: Bundle) {
        title = savedInstanceState.getString(SavedTag.TITLE) ?: NdValue.TEXT
    }

    /**
     * Function for restore content which was passed throw setArgument functions
     * (e.g. position, check and ect.).
     */
    private fun onRestoreInstanceState(bundle: Bundle?) {
        itemList = bundle?.getStringArray(SavedTag.INIT)?.toList() ?: ArrayList()
        position = bundle?.getInt(SavedTag.VALUE) ?: NdValue.POSITION
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog)
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        itemListener?.onClick(dialogInterface, i)
    }
}