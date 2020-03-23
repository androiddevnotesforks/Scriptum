package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem

/**
 * Holder for text note, use in [NoteAdapter]
 */
class NoteTextHolder(
        private val binding: ItemNoteTextBinding,
        private val clickListener: ItemListener.Click,
        private val longClickListener: ItemListener.LongClick?
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.note_text_click_container)

    init {
        clickView.apply {
            setOnClickListener { v ->
                checkNoPosition { clickListener.onItemClick(v, adapterPosition) }
            }

            if (longClickListener == null) return@apply

            setOnLongClickListener { v ->
                checkNoPosition { longClickListener.onItemLongClick(v, adapterPosition) }
                return@setOnLongClickListener true
            }
        }
    }

    fun bind(@Theme theme: Int, item: NoteItem) = binding.apply {
        this.theme = theme
        this.item = item
    }.executePendingBindings()

}