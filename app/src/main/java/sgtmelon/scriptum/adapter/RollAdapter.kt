package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RollReadHolder
import sgtmelon.scriptum.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.view.note.RollNoteFragment

/**
 * Адаптер для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollAdapter(private val rollChangeCallback: RollWriteHolder.RollChange,
                  private val clickListener: ItemListener.Click,
                  private val longClickListener: ItemListener.LongClick
) : ParentAdapter<RollEntity, RecyclerView.ViewHolder>() {

    lateinit var dragListener: ItemListener.Drag
    lateinit var inputCallback: InputCallback

    lateinit var noteState: NoteState

    var checkToggle: Boolean = false
    var cursorPosition = UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == TYPE_READ) {
        RollReadHolder(
                parent.inflateBinding(R.layout.item_roll_read), clickListener, longClickListener
        )
    } else {
        RollWriteHolder(
                parent.inflateBinding(R.layout.item_roll_write),
                dragListener, rollChangeCallback, inputCallback
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        when (holder) {
            is RollReadHolder -> holder.bind(item, noteState, checkToggle)
            is RollWriteHolder -> {
                holder.bind(item)

                if (cursorPosition != UNDEFINED) {
                    holder.setSelections(cursorPosition)
                    cursorPosition = UNDEFINED
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = if (noteState.isEdit) TYPE_WRITE else TYPE_READ

    private companion object {
        private const val UNDEFINED = -1

        private const val TYPE_WRITE = 0
        private const val TYPE_READ = 1
    }

}