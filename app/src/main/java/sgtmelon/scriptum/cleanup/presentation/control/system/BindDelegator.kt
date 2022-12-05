package sgtmelon.scriptum.cleanup.presentation.control.system

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Interface for communicate with [BindDelegatorImpl].
 */
interface BindDelegator {

    fun notifyNotes(itemList: List<NoteItem>)

    fun cancelNote(id: Long)

    fun notifyCount(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clearRecent(@BindDelegatorImpl.Tag tag: String? = null)

}