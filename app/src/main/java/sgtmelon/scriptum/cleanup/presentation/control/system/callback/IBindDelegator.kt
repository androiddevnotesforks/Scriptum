package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.BindDelegator

/**
 * Interface for communicate with [BindDelegator].
 */
interface IBindDelegator {

    fun notifyNotes(itemList: List<NoteItem>)

    fun cancelNote(id: Long)

    fun notifyCount(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clearRecent(@BindDelegator.Tag tag: String? = null)

}