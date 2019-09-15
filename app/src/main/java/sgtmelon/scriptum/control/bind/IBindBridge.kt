package sgtmelon.scriptum.control.bind

import sgtmelon.scriptum.model.NoteModel

/**
 * Callback which need implement in interface what pass to Interactor
 * It's need to get access [BindControl] inside Interactor
 */
interface IBindBridge {

    interface Notify {
        fun notifyBind(noteModel: NoteModel, rankIdVisibleList: List<Long>)
    }

    interface Cancel {
        fun cancelBind(id: Int)
    }

}