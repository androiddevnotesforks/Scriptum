package sgtmelon.scriptum.control.notification

import sgtmelon.scriptum.model.NoteModel

/**
 * Callback which need implement in interface what pass to ViewModel
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