package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.infrastructure.adapter.touch.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.holder.RollReadHolder
import sgtmelon.scriptum.infrastructure.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentAdapter
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Adapter which displays list of [RollItem]'s.
 */
class RollAdapter(
    private var isEdit: Boolean,
    private var state: NoteState,
    private val dragListener: ItemDragListener,
    private val writeCallback: RollWriteHolder.Callback,
    private val readCallback: RollReadHolder.Callback,
    private val onEnterNext: () -> Unit
) : ParentAdapter<RollItem, RecyclerView.ViewHolder>() {

    var cursor = ND_CURSOR

    fun updateEdit(isEdit: Boolean) {
        if (this.isEdit == isEdit) return
        this.isEdit = isEdit
        notifyDataSetChanged()
    }

    fun updateState(state: NoteState) {
        if (this.state == state) return
        this.state = state
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (Type.values()[viewType]) {
            Type.WRITE -> {
                val binding: ItemRollWriteBinding = parent.inflateBinding(R.layout.item_roll_write)
                RollWriteHolder(binding, dragListener, writeCallback, onEnterNext)
            }
            Type.READ -> {
                val binding: ItemRollReadBinding = parent.inflateBinding(R.layout.item_roll_read)
                RollReadHolder(binding, readCallback)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        when (holder) {
            is RollReadHolder -> holder.bind(item, state)
            is RollWriteHolder -> {
                holder.bind(item)

                if (cursor != ND_CURSOR) {
                    holder.setSelections(cursor)
                    cursor = ND_CURSOR
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return (if (isEdit) Type.WRITE else Type.READ).ordinal
    }

    private enum class Type { WRITE, READ }

    private companion object {
        const val ND_CURSOR = -1
    }
}