package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interface for communicate with [BindControl].
 */
interface IBindControl {

    fun notifyNote(noteItem: NoteItem, rankIdVisibleList: List<Long>, @Sort sort: Int)

    fun notifyNote(itemList: List<NoteItem>, rankIdVisibleList: List<Long>? = null,
                   @Sort sort: Int? = null)

    fun cancelNote(id: Long)

    fun notifyInfo(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clearRecent(@BindControl.Tag tag: String? = null)

}