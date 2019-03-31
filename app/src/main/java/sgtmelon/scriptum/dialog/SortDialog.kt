package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.SortAdapter
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.office.annot.def.SortDef
import sgtmelon.scriptum.office.intf.ItemListener

class SortDialog : DialogBlank(), ItemListener.ClickListener {

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

            val position = if (positionFrom == adapter.sortState.end) positionTo
            else positionFrom

            adapter.notifyItemChanged(position)

            keys = listSort.getSort()

            setEnable()
            return true
        }
    }

    fun setArguments(keys: String) {
        val bundle = Bundle()

        bundle.putString(INIT, keys)
        bundle.putString(VALUE, keys)

        arguments = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init = savedInstanceState?.getString(INIT)
                ?: arguments?.getString(INIT)
                ?: ""

        keys = savedInstanceState?.getString(VALUE)
                ?: arguments?.getString(VALUE)
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

        outState.putString(INIT, init)
        outState.putString(VALUE, keys)
    }

    override fun setEnable() {
        super.setEnable()

        buttonPositive.isEnabled = !isSortEqual(init, keys)
        buttonNeutral.isEnabled = !isSortEqual(SortDef.def, keys)
    }

    override fun onItemClick(view: View, p: Int) {
        if (p == RecyclerView.NO_POSITION || p != adapter.sortState.end) return

        val sortItem = listSort[p]

        val key = if (sortItem.key == SortDef.create) SortDef.change else SortDef.create

        sortItem.text = text[key]
        sortItem.key = key

        adapter.notifyItemChanged(sortItem, p)

        keys = listSort.getSort()
        setEnable()
    }

    // TODO revert
    private fun isSortEqual(keys1: String, keys2: String): Boolean {
        val keysArr1 = keys1.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val keysArr2 = keys2.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in keysArr1.indices) {
            if (keysArr1[i] != keysArr2[i]) {
                return false
            }

            if (keysArr1[i] == Integer.toString(SortDef.create) || keysArr1[i] == Integer.toString(SortDef.change)) {
                break
            }
        }

        return true
    }

    /**
     * Получаем строку сортировки
     */
    private fun MutableList<SortItem>.getSort() =
            joinToString(separator = SortDef.divider) { it.key.toString() }

}


