package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.diff.RollDiff
import sgtmelon.scriptum.adapter.holder.RollReadHolder
import sgtmelon.scriptum.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.control.input.IInputControl
import sgtmelon.scriptum.extension.clearAndAdd
import sgtmelon.scriptum.extension.inflateBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment

/**
 * Adapter which displays list of rolls for [RollNoteFragment]
 */
class RollAdapter(
        private val rollWriteCallback: RollWriteHolder.Callback,
        private val clickListener: ItemListener.Click,
        private val longClickListener: ItemListener.LongClick
) : ParentDiffAdapter<RollItem, RollDiff, RecyclerView.ViewHolder>() {

    var dragListener: ItemListener.Drag? = null
    var iInputControl: IInputControl? = null

    var noteState: NoteState? = null

    /**
     * Variable for choose method of set check:
     *
     * TRUE for [CheckBox.toggle].
     * FALSE for [CheckBox.setChecked].
     */
    var checkToggle: Boolean = false
    var cursorPosition = ND_CURSOR


    override val diff = RollDiff()

    override fun setList(list: List<RollItem>) = apply {
        super.setList(list)
        this.list.clearAndAdd(ArrayList(list.map { it.copy() }))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_READ) {
            RollReadHolder(
                    parent.inflateBinding(R.layout.item_roll_read), clickListener, longClickListener
            )
        } else {
            RollWriteHolder(
                    parent.inflateBinding(R.layout.item_roll_write),
                    dragListener, rollWriteCallback, iInputControl
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        when (holder) {
            is RollReadHolder -> holder.bind(item, noteState, checkToggle)
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
        return if (noteState?.isEdit == true) TYPE_WRITE else TYPE_READ
    }


    private companion object {
        const val ND_CURSOR = -1

        const val TYPE_WRITE = 0
        const val TYPE_READ = 1
    }

}