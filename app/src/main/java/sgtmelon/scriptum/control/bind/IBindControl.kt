package sgtmelon.scriptum.control.bind

import sgtmelon.scriptum.model.NoteModel

/**
 * Interface for communicate with [BindControl]
 */
interface IBindControl {

    fun notifyNote(noteModel: NoteModel, rankIdVisibleList: List<Long>)

    fun cancelNote(id: Int)

}