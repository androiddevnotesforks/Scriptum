package sgtmelon.scriptum.control.bind

import sgtmelon.scriptum.model.item.NoteItem

/**
 * Interface for communicate with [BindControl]
 */
interface IBindControl {

    fun notifyNote(noteItem: NoteItem, rankIdVisibleList: List<Long>)

    fun cancelNote(id: Int)

    fun notifyInfo(count: Int)

}