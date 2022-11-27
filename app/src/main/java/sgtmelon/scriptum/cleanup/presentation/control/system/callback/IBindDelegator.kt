package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.BindDelegatorImpl

/**
 * Interface for communicate with [BindDelegatorImpl].
 */
interface IBindDelegator {

    fun notifyNotes(itemList: List<NoteItem>)

    fun cancelNote(id: Long)

    fun notifyCount(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clearRecent(@BindDelegatorImpl.Tag tag: String? = null)

}