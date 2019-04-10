package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.intf.ItemListener

class ColorDialog : DialogBlank() {

    private var init: Int = 0

    var check: Int = 0
        private set

    fun setArguments(check: Int) {
        arguments = Bundle().apply {
            putInt(INIT, check)
            putInt(VALUE, check)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init = savedInstanceState?.getInt(INIT)
                ?: arguments?.getInt(INIT)
                        ?: 0

        check = savedInstanceState?.getInt(VALUE)
                ?: arguments?.getInt(VALUE)
                        ?: 0

        val adapter = ColorAdapter(activity, ItemListener.ClickListener { _, p ->
            check = p
            setEnable()
        })
        adapter.setCheck(check)

        val padding = 24
        val recyclerView = RecyclerView(activity).apply {
            setPadding(padding, padding, padding, padding)
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.column_color))

            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }

        recyclerView.adapter = adapter

        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(INIT, init)
                putInt(VALUE, check)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive.isEnabled = init != check
    }

}