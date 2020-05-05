package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interface for communicate with [BindControl].
 */
interface IBindControl {

    fun notifyNote(@Sort sort: Int, noteItem: NoteItem, rankIdVisibleList: List<Long>)

    fun notifyNote(@Sort sort: Int, itemList: List<NoteItem>, rankIdVisibleList: List<Long>)

    fun cancelNote(id: Int)

    fun notifyInfo(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clearRecent(@BindControl.Tag tag: String? = null)

}