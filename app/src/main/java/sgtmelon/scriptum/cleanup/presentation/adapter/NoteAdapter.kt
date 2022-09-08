package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.extension.inflateBinding
import sgtmelon.scriptum.cleanup.presentation.adapter.diff.NoteDiff
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.NoteRollHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.NoteTextHolder
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity

/**
 * Adapter which displays list of notes for [NotesFragment], [BinFragment], [AlarmActivity]
 */
class NoteAdapter(
    private val clickListener: ItemListener.Click,
    private val longClickListener: ItemListener.LongClick? = null
) : ParentDiffAdapter<NoteItem, NoteDiff, RecyclerView.ViewHolder>() {

    override val diff = NoteDiff()

    override fun setList(list: List<NoteItem>) = apply {
        super.setList(list)
        this.list.clearAdd(ArrayList(list.map {
            return@map when (it) {
                is NoteItem.Text -> it.deepCopy()
                is NoteItem.Roll -> it.deepCopy()
            }
        }))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NoteType.TEXT.ordinal) {
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
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list.getOrNull(position) ?: return

        when (holder) {
            is NoteTextHolder -> if (item is NoteItem.Text) holder.bind(item)
            is NoteRollHolder -> if (item is NoteItem.Roll) holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int) = list[position].type.ordinal

}