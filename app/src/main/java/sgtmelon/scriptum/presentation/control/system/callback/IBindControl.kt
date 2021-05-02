package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.BindControl

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