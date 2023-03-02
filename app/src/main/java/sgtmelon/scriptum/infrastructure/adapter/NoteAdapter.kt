package sgtmelon.scriptum.infrastructure.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.adapter.diff.NoteDiff
import sgtmelon.scriptum.infrastructure.adapter.holder.note.NoteRollHolder
import sgtmelon.scriptum.infrastructure.adapter.holder.note.NoteTextHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentListAdapter
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

/**
 * Adapter which displays list of [NoteItem]'s.
 */
class NoteAdapter(
    private val callback: NoteClickListener
) : ParentListAdapter<NoteItem, RecyclerView.ViewHolder>(NoteDiff()) {

    override fun getListCopy(list: List<NoteItem>): List<NoteItem> {
        return ArrayList(list.map {
            return@map when (it) {
                is NoteItem.Text -> it.copy()
                is NoteItem.Roll -> it.copy()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (NoteType.values()[viewType]) {
            NoteType.TEXT -> NoteTextHolder(parent.inflateBinding(R.layout.item_note_text))
            NoteType.ROLL -> NoteRollHolder(parent.inflateBinding(R.layout.item_note_roll))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is NoteItem.Text -> (holder as? NoteTextHolder)?.bind(item, callback)
            is NoteItem.Roll -> (holder as? NoteRollHolder)?.bind(item, callback)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal

}