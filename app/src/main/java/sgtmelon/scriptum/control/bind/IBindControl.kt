package sgtmelon.scriptum.control.bind

import sgtmelon.scriptum.model.NoteModel

/**
 * Interface for communicate with [BindControl]
 */
interface IBindControl {

    fun notify(noteModel: NoteModel, rankIdVisibleList: List<Long>)

    fun cancel(id: Int)

}