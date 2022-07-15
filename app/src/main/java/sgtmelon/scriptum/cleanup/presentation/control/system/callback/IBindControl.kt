package sgtmelon.scriptum.cleanup.presentation.control.system.callback

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.BindControl

/**
 * Interface for communicate with [BindControl].
 */
interface IBindControl {

    fun notifyNotes(itemList: List<NoteItem>)

    fun cancelNote(id: Long)

    fun notifyCount(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clearRecent(@BindControl.Tag tag: String? = null)

}