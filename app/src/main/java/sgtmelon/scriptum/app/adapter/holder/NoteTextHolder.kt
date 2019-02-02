package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Держатель заметки-текста для [NoteAdapter]
 */
class NoteTextHolder(private val binding: ItemNoteTextBinding,
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

    fun bind(noteItem: NoteItem) {
        binding.noteItem = noteItem
        binding.executePendingBindings()
    }

}