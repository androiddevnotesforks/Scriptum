package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.utils.AppUtils.checkNoPosition

/**
 * Держатель заметки-списка для [NoteAdapter]
 */
class NoteRollHolder(private val binding: ItemNoteRollBinding,
                     private val clickListener: ItemIntf.ClickListener,
                     private val longClickListener: ItemIntf.LongClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.note_roll_click_container)

    init {
        clickView.setOnClickListener { v ->
            checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
        }
        clickView.setOnLongClickListener { v ->
            checkNoPosition { longClickListener.onItemLongClick(v, adapterPosition) }
            return@setOnLongClickListener true
        }
    }

    fun bind(noteItem: NoteItem, listRoll: List<RollItem>) {
        binding.noteItem = noteItem
        binding.listRoll = listRoll

        binding.executePendingBindings()
    }

}