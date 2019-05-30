package sgtmelon.scriptum.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NoteAdapter
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.checkNoPosition

/**
 * Держатель заметки-текста для [NoteAdapter]
 *
 * @author SerjantArbuz
 */
class NoteTextHolder(private val binding: ItemNoteTextBinding,
                     private val clickListener: ItemListener.ClickListener,
                     private val longClickListener: ItemListener.LongClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.note_text_click_container)

    init {
        clickView.apply {
            setOnClickListener { v -> checkNoPosition { clickListener.onItemClick(v, adapterPosition) } }

            if (longClickListener == null) return@apply

            setOnLongClickListener { v ->
                checkNoPosition { longClickListener.onItemLongClick(v, adapterPosition) }
                return@setOnLongClickListener true
            }
        }
    }

    fun bind(noteItem: NoteItem) = binding.apply {
        this.noteItem = noteItem
    }.executePendingBindings()

}