package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Держатель заметки-списка для [NoteAdapter]
 */
class NoteRollHolder(private val binding: ItemNoteRollBinding,
                     private val clickListener: ItemIntf.ClickListener,
                     private val longClickListener: ItemIntf.LongClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.click_container)

    init {
        clickView.setOnClickListener { v -> clickListener.onItemClick(v, adapterPosition) }
        clickView.setOnLongClickListener { v ->
            longClickListener.onItemLongClick(v, adapterPosition)
        }
    }

    fun bind(noteItem: NoteItem, listRoll: List<RollItem>) {
        binding.noteItem = noteItem
        binding.listRoll = listRoll

        binding.executePendingBindings()
    }

}