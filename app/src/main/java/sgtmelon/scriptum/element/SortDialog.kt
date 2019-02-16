package sgtmelon.scriptum.element

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.office.annot.DialogAnn
import sgtmelon.safedialog.office.blank.DialogBlank
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.SortAdapter
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.utils.HelpUtils
import sgtmelon.scriptum.office.utils.HelpUtils.Sort.getSort

class SortDialog : DialogBlank(), ItemIntf.ClickListener {

    private val listSort: MutableList<SortItem> = ArrayList()

    private var init: String = ""

    var keys: String = ""
        private set

    private lateinit var text: Array<String>
    private lateinit var adapter: SortAdapter

    private val touchCallback = object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView,
                                      viewHolder: RecyclerView.ViewHolder): Int {
            val flagsDrag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val flagsSwipe = 0

            return ItemTouchHelper.Callback.makeMovementFlags(flagsDrag, flagsSwipe)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            val positionFrom = viewHolder.adapterPosition
            val positionTo = target.adapterPosition

            val sortItem = listSort[positionFrom]

            listSort.removeAt(positionFrom)
            listSort.add(positionTo, sortItem)

            adapter.setList(listSort)
            adapter.notifyItemMoved(positionFrom, positionTo)

            val position = if (positionFrom == adapter.sortSt.end) positionTo
            else positionFrom

            adapter.notifyItemChanged(position)

            keys = HelpUtils.Sort.getSortByList(listSort)

            setEnable()
            return true
        }
    }

    fun setArguments(keys: String) {
        val bundle = Bundle()

        bundle.putString(DialogAnn.INIT, keys)
        bundle.putString(DialogAnn.VALUE, keys)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init = savedInstanceState?.getString(DialogAnn.INIT)
                ?: arguments?.getString(DialogAnn.INIT)
                ?: ""

        keys = savedInstanceState?.getString(DialogAnn.VALUE)
                ?: arguments?.getString(DialogAnn.VALUE)
                ?: ""

        text = resources.getStringArray(R.array.pref_sort_text)

        val recyclerView = RecyclerView(activity)

        val padding = activity.resources.getInteger(R.integer.dlg_recycler_padding)
        recyclerView.setPadding(padding, padding, padding, padding)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = SortAdapter(activity, this)

        listSort.clear()
        for (aKey in keys.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val key = Integer.parseInt(aKey)
            val sortItem = SortItem(text[key], key)
            listSort.add(sortItem)
        }

        adapter.setList(listSort)

        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(touchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val animator = recyclerView.itemAnimator as SimpleItemAnimator?
        if (animator != null) {
            animator.supportsChangeAnimations = false
        }

        return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.dialog_title_sort))
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setNeutralButton(getString(R.string.dialog_btn_reset), onNeutralClick)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(DialogAnn.INIT, init)
        outState.putString(DialogAnn.VALUE, keys)
    }

    override fun setEnable() {
        super.setEnable()

        buttonPositive.isEnabled = !HelpUtils.Sort.getSortEqual(init, keys)
        buttonNeutral.isEnabled = !HelpUtils.Sort.getSortEqual(SortDef.def, keys)
    }

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION || p != adapter.sortSt.end) return

        val sortItem = listSort[p]

        val key = if (sortItem.key == SortDef.create) SortDef.change
        else SortDef.create

        sortItem.text = text[key]
        sortItem.key = key

        adapter.setListItem(p, sortItem)
        adapter.notifyItemChanged(p)

        keys = listSort.getSort()
        setEnable()
    }

}


