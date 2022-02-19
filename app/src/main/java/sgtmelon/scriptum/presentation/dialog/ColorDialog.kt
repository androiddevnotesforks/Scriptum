package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.applyAnimation
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.presentation.adapter.ColorAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener

class ColorDialog : BlankDialog(), ItemListener.Click {

    private var init: Int = NdValue.CHECK
    var check: Int = NdValue.CHECK
        private set

    fun setArguments(@Color check: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.INIT, check)
            putInt(SavedTag.VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val context = context as Context
        val padding = 24
        val recyclerView = RecyclerView(context).apply {
            id = R.id.color_recycler_view

            setPadding(padding, padding, padding, padding)
            overScrollMode = View.OVER_SCROLL_NEVER

            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.color_column_count))

            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

            adapter = ColorAdapter(clickListener = this@ColorDialog).setCheck(check)
        }

        return AlertDialog.Builder(context)
            .setTitle(title)
            .setView(recyclerView)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        super.onRestoreInstanceState(bundle)
        init = bundle?.getInt(SavedTag.INIT) ?: NdValue.CHECK
        check = bundle?.getInt(SavedTag.VALUE) ?: NdValue.CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SavedTag.INIT, init)
        outState.putInt(SavedTag.VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()
        positiveButton?.isEnabled = init != check
    }

    override fun onItemClick(view: View, p: Int) {
        check = p
        setEnable()
    }
}