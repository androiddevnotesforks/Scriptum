package sgtmelon.scriptum.presentation.control.system.callback

import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interface for communicate with [BindControl].
 */
interface IBindControl {

    fun notifyNote(noteItem: NoteItem, rankIdVisibleList: List<Long>)

    fun cancelNote(id: Int)

    fun notifyInfo(count: Int)

    /**
     * Clear notifications on test tearDown.
     */
    fun clear()

}