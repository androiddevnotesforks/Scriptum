package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemNoteRollBinding

/**
 * Держатель заметки-списка для [NoteAdapter]
 */
class NoteRollHolder(private val binding: ItemNoteRollBinding) : RecyclerView.ViewHolder(binding.root) {

    val clickView: View = itemView.findViewById(R.id.click_container)

    fun bind(noteItem: NoteItem, listRoll: List<RollItem>) {
        binding.noteItem = noteItem
        binding.listRoll = listRoll

        binding.executePendingBindings()
    }

}