package sgtmelon.scriptum.widget.color

import android.app.Dialog
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R

class ColorDialog : DialogBlank(), ColorIntf.ClickListener {

    private var init: Int = 0

    var check: Int = 0
        private set

    fun setArguments(check: Int) {
        val bundle = Bundle()

        bundle.putInt(INIT, check)
        bundle.putInt(VALUE, check)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getInt(INIT)
                ?: bundle?.getInt(INIT)
                ?: 0

        check = savedInstanceState?.getInt(VALUE)
                ?: bundle?.getInt(VALUE)
                ?: 0

        val adapter = ColorAdapter(activity)
        adapter.setCheck(check)
        adapter.setClickListener(this)

        val padding = 24

        val recyclerView = RecyclerView(activity)
        recyclerView.setPadding(padding, padding, padding, padding)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.layoutManager = GridLayoutManager(activity, activity.resources.getInteger(R.integer.column_color))
        recyclerView.adapter = adapter
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        return AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(INIT, init)
        outState.putInt(VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()
        buttonPositive.isEnabled = init != check
    }

    override fun onColorClick(p: Int) {
        check = p
        setEnable()
    }

}