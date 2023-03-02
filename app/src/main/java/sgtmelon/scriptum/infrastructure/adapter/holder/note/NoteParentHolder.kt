package sgtmelon.scriptum.infrastructure.adapter.holder.note

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.bindIndicatorColor
import sgtmelon.scriptum.cleanup.extension.bindNoteColor
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.NoteClickListener
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.utils.extensions.makeGone
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf

/**
 * Parent [RecyclerView.ViewHolder] for all types of note.
 */
abstract class NoteParentHolder<T: NoteItem>(itemView: View) : ParentHolder(itemView),
    UnbindCallback {

    /** Common views for all notes. */
    abstract val parentCard: CardView
    abstract val clickContainer: ViewGroup
    abstract val nameText: TextView
    abstract val colorView: View

    open fun bind(item: T, callback: NoteClickListener) {
        bindTheme(item)
        bindName(item)
        bindContent(item)
        bindInfo(item)
        bindClick(item, callback)
    }

    /**
     * [ThemeDisplayed.LIGHT] - set color only for card and hide indicator.
     * [ThemeDisplayed.DARK]  - set color for indicator, don't set anything for card
     */
    private fun bindTheme(item: NoteItem) {
        when (context.getDisplayedTheme() ?: return) {
            ThemeDisplayed.LIGHT -> {
                parentCard.bindNoteColor(item.color)
                colorView.makeGone()
            }
            ThemeDisplayed.DARK -> {
                colorView.makeVisible()
                colorView.bindIndicatorColor(item.color)
            }
        }
    }

    private fun bindName(item: T) {
        nameText.text = item.name
        nameText.makeVisibleIf(item.name.isNotEmpty())
    }

    /** Views between [nameText] and bottom short information (will be setup in [bindInfo]). */
    abstract fun bindContent(item: T)

    /** Views with short information like: date, rank, bind, ect. */
    abstract fun bindInfo(item: T)

    private fun bindClick(item: T, callback: NoteClickListener) {
        clickContainer.setOnClickListener { callback.onNoteClick(item) }
        clickContainer.setOnLongClickListener {
            checkPosition { callback.onNoteLongClick(item, it) }
            return@setOnLongClickListener true
        }
    }

    override fun unbind() {
        clickContainer.setOnClickListener(null)
        clickContainer.setOnLongClickListener(null)
    }
}