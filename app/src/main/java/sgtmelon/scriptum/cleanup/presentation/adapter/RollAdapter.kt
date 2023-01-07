package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RollReadHolder
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentAdapter
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.isTrue

/**
 * Adapter which displays list of rolls for [RollNoteFragmentImpl].
 */
class RollAdapter(
    private val dragListener: ItemDragListener,
    private val writeCallback: RollWriteHolder.Callback,
    private val readCallback: RollReadHolder.Callback
) : ParentAdapter<RollItem, RecyclerView.ViewHolder>() {

    lateinit var history: NoteHistory

    var isEdit: Boolean? = null
    var state: NoteState? = null

    var cursorPosition = ND_CURSOR

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Type.WRITE -> RollWriteHolder(
            parent.inflateBinding(R.layout.item_roll_write),
            dragListener, writeCallback, history
        )
        else -> RollReadHolder(parent.inflateBinding(R.layout.item_roll_read), readCallback)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        when (holder) {
            is RollReadHolder -> holder.bind(item, state)
            is RollWriteHolder -> {
                holder.bind(item)

                if (cursorPosition != ND_CURSOR) {
                    holder.setSelections(cursorPosition)
                    cursorPosition = ND_CURSOR
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEdit.isTrue()) Type.WRITE else Type.READ
    }

    @IntDef(Type.WRITE, Type.READ)
    private annotation class Type {
        companion object {
            const val WRITE = 0
            const val READ = 1
        }
    }

    private companion object {
        const val ND_CURSOR = -1
    }
}