package sgtmelon.scriptum.infrastructure.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankButtonDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.adapter.ColorAdapter
import sgtmelon.scriptum.infrastructure.adapter.callback.click.ColorClickListener
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.disableChangeAnimations

/**
 * Dialog for display application available colors for notes.
 */
class ColorDialog : BlankButtonDialog(),
    ColorClickListener {

    private var checkInit: Int = DEF_CHECK
    var check: Int = DEF_CHECK
        private set

    fun setArguments(color: Color) = apply {
        val check = color.ordinal

        arguments = Bundle().apply {
            putInt(SavedTag.Common.VALUE_INIT, check)
            putInt(SavedTag.Common.VALUE, check)
        }
    }

    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setView(createRecycler(context))
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
    }

    private fun createRecycler(context: Context): View {
        val recyclerView = RecyclerView(context)

        recyclerView.id = R.id.color_recycler_view

        val padding = resources.getInteger(R.integer.recycler_color_padding)
        recyclerView.setPadding(padding, padding, padding, padding)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER

        val columnCount = resources.getInteger(R.integer.color_column_count)
        recyclerView.layoutManager = GridLayoutManager(context, columnCount)
        recyclerView.adapter = ColorAdapter(callback = this@ColorDialog, check)

        recyclerView.disableChangeAnimations()

        /** The height of all items absolutely the same. */
        recyclerView.setHasFixedSize(true)

        return recyclerView
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        checkInit = bundle?.getInt(SavedTag.Common.VALUE_INIT) ?: DEF_CHECK
        check = bundle?.getInt(SavedTag.Common.VALUE) ?: DEF_CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SavedTag.Common.VALUE_INIT, checkInit)
        outState.putInt(SavedTag.Common.VALUE, check)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()
        positiveButton?.isEnabled = checkInit != check
    }

    override fun onColorClick(p: Int) {
        check = p
        changeButtonEnable()
    }

    companion object {
        private const val DEF_CHECK = 0
    }
}