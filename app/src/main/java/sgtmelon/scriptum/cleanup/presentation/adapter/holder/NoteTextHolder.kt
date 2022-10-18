package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class NoteTextHolder(
    private val binding: ItemNoteTextBinding
) : ParentHolder(binding.root),
    UnbindCallback {

    private val clickView: View = itemView.findViewById(R.id.note_text_click_container)

    fun bind(item: NoteItem.Text, callback: NoteClickListener) {
        clickView.setOnClickListener { callback.onNoteClick(item) }
        clickView.setOnLongClickListener {
            checkPosition { callback.onNoteLongClick(item, it) }
            return@setOnLongClickListener true
        }

        // TODO remove databinding and use only view binding
        binding.apply { this.item = item }.executePendingBindings()
    }

    override fun unbind() {
        clickView.setOnClickListener(null)
        clickView.setOnLongClickListener(null)
    }
}