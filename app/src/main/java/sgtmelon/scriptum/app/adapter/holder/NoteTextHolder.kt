package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.AppUtils.checkNoPosition

/**
 * Держатель заметки-текста для [NoteAdapter]
 */
class NoteTextHolder(private val binding: ItemNoteTextBinding,
                     private val clickListener: ItemListener.ClickListener,
                     private val longClickListener: ItemListener.LongClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.note_text_click_container)

    init {
        clickView.setOnClickListener {v ->
            checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
        }
        clickView.setOnLongClickListener {v ->
            checkNoPosition { longClickListener.onItemLongClick(v, adapterPosition) }
            return@setOnLongClickListener true
        }
    }

    fun bind(noteItem: NoteItem) {
        binding.noteItem = noteItem
        binding.executePendingBindings()
    }

}