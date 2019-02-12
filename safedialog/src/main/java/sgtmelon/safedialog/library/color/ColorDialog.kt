package sgtmelon.safedialog.library.color

import android.app.Dialog
import android.os.Bundle
import android.view.View

import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.R
import sgtmelon.safedialog.office.annot.DialogAnn
import sgtmelon.safedialog.office.blank.DialogBlank
import sgtmelon.safedialog.office.intf.ColorIntf

class ColorDialog : DialogBlank(), ColorIntf.ClickListener {

    private var init: Int = 0
    var check: Int = 0
        private set

    @IdRes var fillColor: IntArray? = null
    @IdRes var strokeColor: IntArray? = null
    @IdRes var checkColor: IntArray? = null
    var columnCount: Int = 0

    fun setArguments(check: Int) {
        val bundle = Bundle()

        bundle.putInt(DialogAnn.INIT, check)
        bundle.putInt(DialogAnn.VALUE, check)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getInt(DialogAnn.INIT)
                ?: bundle?.getInt(DialogAnn.INIT)
                ?: 0

        check = savedInstanceState?.getInt(DialogAnn.VALUE)
                ?: bundle?.getInt(DialogAnn.VALUE)
                ?: 0

        if (fillColor == null || strokeColor == null || checkColor == null) {
            throw NullPointerException("One of color arrays is null")
        } else if (fillColor!!.size != strokeColor!!.size && strokeColor!!.size != checkColor!!.size) {
            throw ArrayIndexOutOfBoundsException("Color arrays should have equal length")
        }

        val recyclerView = RecyclerView(context!!)

        val padding = 24
        recyclerView.setPadding(padding, padding, padding, padding)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER

        recyclerView.layoutManager = GridLayoutManager(context, columnCount)

        val adapter = ColorAdapter(context, fillColor!!.size)
        adapter.setFillColor(fillColor)
        adapter.setStrokeColor(strokeColor)
        adapter.setCheckColor(checkColor)
        adapter.setCheck(check)
        adapter.setClickListener(this)

        recyclerView.adapter = adapter

        val animator = recyclerView.itemAnimator as SimpleItemAnimator?
        animator?.supportsChangeAnimations = false

        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(DialogAnn.INIT, init)
        outState.putInt(DialogAnn.VALUE, check)
    }

    override fun setEnable() {
        super.setEnable()

        buttonPositive!!.isEnabled = init != check
    }

    override fun onColorClick(p: Int) {
        check = p
        setEnable()
    }

}