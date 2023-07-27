package sgtmelon.scriptum.infrastructure.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollContentBinding
import sgtmelon.scriptum.infrastructure.adapter.diff.manual.RollContentDiff
import sgtmelon.scriptum.infrastructure.adapter.holder.RollContentHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentManualAdapter
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.ItemDragListener
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Adapter which displays list of [RollItem]'s.
 */
class RollContentAdapter(
    private var isEdit: Boolean,
    private var state: NoteState,
    private val readCallback: RollContentHolder.ReadCallback,
    private val writeCallback: RollContentHolder.WriteCallback,
    private val dragListener: ItemDragListener,
    private val onEnterNext: () -> Unit
) : ParentManualAdapter<RollItem, RollContentHolder>(RollContentDiff()) {

    init {
        setHasStableIds(true)
    }

    //region Custom functions

    /** Pass this cursor into the next one notified [RollContentHolder]. Reset to default after get. */
    private var cursor: Int? = null
        get() {
            val value = field
            cursor = null
            return value
        }

    fun setCursor(cursor: Int) = apply { this.cursor = cursor }

    @SuppressLint("NotifyDataSetChanged")
    fun updateState(state: NoteState) {
        if (this.state != state) {
            this.state = state
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEdit(isEdit: Boolean) {
        if (this.isEdit != isEdit) {
            this.isEdit = isEdit
            notifyDataSetChanged()
        }
    }

    //endregion

    override fun getListCopy(list: List<RollItem>): List<RollItem> {
        return ArrayList(list.map { it.copy() })
    }

    override fun getItemId(position: Int): Long = getItem(position).uniqueId.hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollContentHolder {
        val binding: ItemRollContentBinding = parent.inflateBinding(R.layout.item_roll_content)
        return RollContentHolder(binding, readCallback, writeCallback, dragListener, onEnterNext)
    }

    override fun onBindViewHolder(holder: RollContentHolder, position: Int) {
        holder.bind(isEdit, state, getItem(position))
        cursor?.let { holder.bindSelection(it) }
    }
}