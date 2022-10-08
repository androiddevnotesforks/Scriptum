package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.checkNoPosition
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.NoteClickListener
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback

class NoteTextHolder(
    private val binding: ItemNoteTextBinding
) : RecyclerView.ViewHolder(binding.root),
    UnbindCallback {

    private val clickView: View = itemView.findViewById(R.id.note_text_click_container)

    fun bind(item: NoteItem.Text, callback: NoteClickListener) {
        clickView.setOnClickListener { callback.onItemClick(item) }
        clickView.setOnLongClickListener {
            checkNoPosition { callback.onItemLongClick(item, it) }
            return@setOnLongClickListener true
        }

        binding.apply { this.item = item }.executePendingBindings()
    }

    override fun unbind() {
        clickView.setOnClickListener(null)
        clickView.setOnLongClickListener(null)
    }
}