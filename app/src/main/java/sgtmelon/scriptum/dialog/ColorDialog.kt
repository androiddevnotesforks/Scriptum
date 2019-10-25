package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.BlankDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.ColorAdapter
import sgtmelon.scriptum.listener.ItemListener

class ColorDialog : BlankDialog() {

    private var init: Int = 0

    var check: Int = 0
        private set

    fun setArguments(check: Int) = apply {
        arguments = Bundle().apply {
            putInt(INIT, check)
            putInt(VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init = savedInstanceState?.getInt(INIT) ?: arguments?.getInt(INIT) ?: 0
        check = savedInstanceState?.getInt(VALUE) ?: arguments?.getInt(VALUE) ?: 0

        val context = context as Context

        val padding = 24
        val recyclerView = RecyclerView(context).apply {
            id = R.id.color_recycler_view

            setPadding(padding, padding, padding, padding)
            overScrollMode = View.OVER_SCROLL_NEVER

            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.color_column_count))

            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

            adapter = ColorAdapter(context, object : ItemListener.Click {
                override fun onItemClick(view: View, p: Int) {
                    check = p
                    setEnable()
                }
            }).setCheck(check)
        }

        return AlertDialog.Builder(context)
                .setTitle(title)
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(INIT, init)
            putInt(VALUE, check)
        })
    }

    override fun setEnable() {
        super.setEnable()
        positiveButton?.isEnabled = init != check
    }

}