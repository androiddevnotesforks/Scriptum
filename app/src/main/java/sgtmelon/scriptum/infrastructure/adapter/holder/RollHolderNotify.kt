package sgtmelon.scriptum.infrastructure.adapter.holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.model.key.NoteState

/**
 * Interface for access different notify function directly in [ViewHolder].
 */
interface RollHolderNotify {

    fun bindEdit(isEdit: Boolean, item: RollItem)

    fun bindState(state: NoteState)

}