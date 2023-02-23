package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollBinding
import sgtmelon.scriptum.infrastructure.adapter.diff.RollDiff
import sgtmelon.scriptum.infrastructure.adapter.holder.RollHolder
import sgtmelon.scriptum.infrastructure.adapter.holder.RollHolderNotify
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentDiffAdapter
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.ItemDragListener
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Adapter which displays list of [RollItem]'s.
 */
class RollAdapter(
    private var isEdit: Boolean,
    private var state: NoteState,
    private val readCallback: RollHolder.ReadCallback,
    private val writeCallback: RollHolder.WriteCallback,
    private val dragListener: ItemDragListener,
    private val onEnterNext: () -> Unit
) : ParentDiffAdapter<RollItem, RollHolder>(RollDiff()) {

    override fun getListCopy(list: List<RollItem>): List<RollItem> {
        val copyList = ArrayList(list.map { it.copy() })

        /**
         * Need search for items and update them in [notifyMap]. For correct work
         * of [updateEdit].
         */
        notifyMap.forEach { (notify, item) ->
            /** If didn't find item in list -> it was deleted. */
            val newItem = copyList.firstOrNull { item.uniqueId == it.uniqueId }

            if (newItem != null) {
                notifyMap[notify] = newItem
            } else {
                notifyMap.remove(notify)
            }
        }

        return copyList
    }

    /** Pass this cursor into the next one notified [RollHolder]. Reset to default after get. */
    private var cursor: Int? = null
        get() {
            val value = field
            cursor = null
            return value
        }

    fun setCursor(cursor: Int) = apply { this.cursor = cursor }

    /**
     * This map needed fast [RollHolder] bindings and skip adapter standard notify functions.
     * Make UI more smooth.
     */
    private val notifyMap = mutableMapOf<RollHolderNotify, RollItem>()

    fun updateEdit(isEdit: Boolean) {
        if (this.isEdit != isEdit) {
            this.isEdit = isEdit
            notifyMap.forEach { (notify, it) -> notify.bindEdit(isEdit, it) }
        }
    }

    fun updateState(state: NoteState) {
        if (this.state != state) {
            this.state = state
            notifyMap.forEach { (notify, _) -> notify.bindState(state) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollHolder {
        val binding: ItemRollBinding = parent.inflateBinding(R.layout.item_roll)
        return RollHolder(binding, readCallback, writeCallback, dragListener, onEnterNext)
    }

    override fun onBindViewHolder(holder: RollHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.bindEdit(isEdit, item)
        holder.bindState(state)
        cursor?.let { holder.bindSelection(it) }

        notifyMap[holder] = item
    }

    override fun onViewRecycled(holder: RollHolder) {
        super.onViewRecycled(holder)
        notifyMap.remove(holder)
    }
}