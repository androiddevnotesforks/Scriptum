package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.domain.model.state.NoteState
import sgtmelon.scriptum.cleanup.extension.inflateBinding
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RollReadHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment

/**
 * Adapter which displays list of rolls for [RollNoteFragment].
 */
class RollAdapter(
    private val rollWriteCallback: RollWriteHolder.Callback,
    private val clickListener: ItemListener.ActionClick
) : ParentAdapter<RollItem, RecyclerView.ViewHolder>() {

    var dragListener: ItemListener.Drag? = null
    var inputControl: IInputControl? = null

    var noteState: NoteState? = null

    var cursorPosition = ND_CURSOR

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Type.WRITE -> RollWriteHolder(
            parent.inflateBinding(R.layout.item_roll_write),
            dragListener, rollWriteCallback, inputControl
        )
        else -> RollReadHolder(parent.inflateBinding(R.layout.item_roll_read), clickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        when (holder) {
            is RollReadHolder -> holder.bind(item, noteState)
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
        return if (noteState?.isEdit == true) Type.WRITE else Type.READ
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