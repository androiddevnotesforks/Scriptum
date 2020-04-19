package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.checkNoPosition
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
import sgtmelon.scriptum.presentation.listener.ItemListener

/**
 * Holder for roll note, use in [NoteAdapter]
 */
class NoteRollHolder(
        private val binding: ItemNoteRollBinding,
        private val clickListener: ItemListener.Click,
        private val longClickListener: ItemListener.LongClick?
) : RecyclerView.ViewHolder(binding.root) {

    private val clickView: View = itemView.findViewById(R.id.note_roll_click_container)

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

    fun bind(@Theme theme: Int, item: NoteItem.Roll) = binding.apply {
        this.theme = theme
        this.item = item
    }.executePendingBindings()

}