package sgtmelon.scriptum.presentation.service.presenter

import sgtmelon.scriptum.presentation.service.IEternalService

/**
 * Presenter for [IEternalService].
 */
class EternalPresenter : ParentPresenter<IEternalService>(),
    IEternalPresenter {

    override fun notifyNoteList() {
        TODO("Not yet implemented")
    }

    override fun cancelNote(id: Long) {
        TODO("Not yet implemented")
    }

    override fun notifyInfo(count: Int) {
        TODO("Not yet implemented")
    }
}