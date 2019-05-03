package sgtmelon.scriptum.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.checkNoPosition

/**
 * Держатель заметки-списка для [NoteAdapter]
 *
 * @author SerjantArbuz
 */
class NoteRollHolder(private val binding: ItemNoteRollBinding,
                     private val clickListener: ItemListener.ClickListener,
                     private val longClickListener: ItemListener.LongClickListener
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