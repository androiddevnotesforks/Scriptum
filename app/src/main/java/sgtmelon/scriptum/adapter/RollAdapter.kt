package sgtmelon.scriptum.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.RollReadHolder
import sgtmelon.scriptum.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.inflateBinding
import sgtmelon.scriptum.screen.view.note.RollNoteFragment

/**
 * Адаптер для [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollAdapter(context: Context,
                  private val clickListener: ItemListener.ClickListener,
                  private val textChangeCallback: RollWriteHolder.TextChange
) : ParentAdapter<RollItem, RecyclerView.ViewHolder>(context) {

    lateinit var dragListener: ItemListener.DragListener
    lateinit var inputCallback: InputCallback

    lateinit var noteState: NoteState

    var checkToggle: Boolean = false
    var cursorPosition = UNDEFINED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_READ) {
            RollReadHolder(inflater.inflateBinding(R.layout.item_roll_read, parent), clickListener)
        } else {
            RollWriteHolder(
                    inflater.inflateBinding(R.layout.item_roll_write, parent),
                    dragListener, textChangeCallback, inputCallback
            )
        }
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