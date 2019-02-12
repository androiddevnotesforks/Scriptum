package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.NoteRollHolder
import sgtmelon.scriptum.app.adapter.holder.NoteTextHolder
import sgtmelon.scriptum.app.model.NoteRepo
import sgtmelon.scriptum.app.view.fragment.main.BinFragment
import sgtmelon.scriptum.app.view.fragment.main.NotesFragment
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.office.annot.def.TypeNoteDef
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Адаптер списка заметок для [NotesFragment], [BinFragment]
 */
class NoteAdapter(context: Context,
                  clickListener: ItemIntf.ClickListener,
                  private val longClickListener: ItemIntf.LongClickListener
) : ParentAdapter<NoteRepo, RecyclerView.ViewHolder>(context, clickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TypeNoteDef.text -> {
                val bindingText = DataBindingUtil.inflate<ItemNoteTextBinding>(
                        inflater, R.layout.item_note_text, parent, false
                )

                NoteTextHolder(bindingText, clickListener, longClickListener)
            }
            else -> {
                val bindingRoll = DataBindingUtil.inflate<ItemNoteRollBinding>(
                        inflater, R.layout.item_note_roll, parent, false
                )

                NoteRollHolder(bindingRoll, clickListener, longClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val noteRepo = list[position]

        if (holder is NoteTextHolder) {
            holder.bind(noteRepo.noteItem)
        } else if (holder is NoteRollHolder) {
            holder.bind(noteRepo.noteItem, noteRepo.listRoll)
        }
    }

    override fun getItemViewType(position: Int) = list[position].noteItem.type.ordinal

}