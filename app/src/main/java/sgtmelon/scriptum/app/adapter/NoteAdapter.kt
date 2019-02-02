package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.NoteHolder
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.fragment.BinFragment
import sgtmelon.scriptum.app.view.fragment.NotesFragment
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.office.annot.def.TypeNoteDef

/**
 * Адаптер списка заметок для [NotesFragment], [BinFragment]
 */
class NoteAdapter(context: Context) : ParentAdapter<NoteRepo, NoteHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return if (viewType == TypeNoteDef.text) {
            val bindingText = DataBindingUtil.inflate<ItemNoteTextBinding>(
                    inflater, R.layout.item_note_text, parent, false
            )

            NoteHolder(bindingText)
        } else {
            val bindingRoll = DataBindingUtil.inflate<ItemNoteRollBinding>(
                    inflater, R.layout.item_note_roll, parent, false
            )

            NoteHolder(bindingRoll)
        }
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val noteRepo = list[position]

        holder.bind(noteRepo.noteItem, noteRepo.listRoll)

        holder.clickView.setOnClickListener { v -> clickListener.onItemClick(v, position) }
        holder.clickView.setOnLongClickListener { v -> longClickListener.onItemLongClick(v, position) }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].noteItem.type
    }

}