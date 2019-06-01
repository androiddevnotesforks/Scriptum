package sgtmelon.scriptum.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.NoteRollHolder
import sgtmelon.scriptum.adapter.holder.NoteTextHolder
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.inflateBinding
import sgtmelon.scriptum.screen.view.main.BinFragment
import sgtmelon.scriptum.screen.view.main.NotesFragment

/**
 * Адаптер списка заметок для [NotesFragment], [BinFragment]
 *
 * @author SerjantArbuz
 */
class NoteAdapter(@Theme private val theme: Int,
                  private val clickListener: ItemListener.Click,
                  private val longClickListener: ItemListener.LongClick? = null
) : ParentAdapter<NoteModel, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            if (viewType == NoteType.TEXT.ordinal) {
                NoteTextHolder(
                        parent.inflateBinding(R.layout.item_note_text),
                        clickListener, longClickListener
                )
            } else {
                NoteRollHolder(
                        parent.inflateBinding(R.layout.item_note_roll),
                        clickListener, longClickListener
                )
            }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            with(list[position]) {
                when (holder) {
                    is NoteTextHolder -> holder.bind(theme, noteItem)
                    is NoteRollHolder -> holder.bind(theme, noteItem, rollList)
                }
            }

    override fun getItemViewType(position: Int) = list[position].noteItem.type.ordinal

}