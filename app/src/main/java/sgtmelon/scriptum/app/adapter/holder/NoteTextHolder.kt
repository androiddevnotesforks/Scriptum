package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.databinding.ItemNoteTextBinding

/**
 * Держатель заметки-текста для [NoteAdapter]
 */
class NoteTextHolder(private val binding: ItemNoteTextBinding) : RecyclerView.ViewHolder(binding.root) {

    val clickView: View = itemView.findViewById(R.id.click_container)

    fun bind(noteItem: NoteItem) {
        binding.noteItem = noteItem
        binding.executePendingBindings()
    }

}