package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener

/**
 * Holder for text note, use in [NoteAdapter]
 */
class NoteTextHolder(
    private val binding: ItemNoteTextBinding,
    private val clickListener: ItemListener.Click,
    private val longClickListener: ItemListener.LongClick?
) : ParentHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.note_text_click_container)

    init {
        clickView.apply {
            setOnClickListener { v -> checkNoPosition { clickListener.onItemClick(v, it) } }

            if (longClickListener == null) return@apply

            setOnLongClickListener { v ->
                checkNoPosition { longClickListener.onItemLongClick(v, it) }
            }
        }
    }

    fun bind(item: NoteItem.Text) = binding.apply { this.item = item }.executePendingBindings()

}