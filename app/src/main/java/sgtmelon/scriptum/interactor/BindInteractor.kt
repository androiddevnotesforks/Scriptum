package sgtmelon.scriptum.interactor

import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo

/**
 * Interactor for binding notification in status bar.
 */
class BindInteractor(
        private val iPreferenceRepo: IPreferenceRepo,
        private val iBindRepo: IBindRepo,
        private val iRankRepo: IRankRepo,
        private val iNoteRepo: INoteRepo
) : ParentInteractor(),
        IBindInteractor {

    /**
     * Update all bind notes in status bar rely on rank visibility.
     */
    override suspend fun notifyNoteBind(callback: BindControl.NoteBridge.Notify?) {
        val rankIdVisibleList = iRankRepo.getIdVisibleList()

        val sort = iPreferenceRepo.sort
        iNoteRepo.getList(sort, bin = false, optimal = false, filterVisible = false).forEach {
            callback?.notifyNoteBind(it, rankIdVisibleList)
        }
    }

    override suspend fun notifyInfoBind(callback: BindControl.InfoBridge?) {
        callback?.notifyInfoBind(iBindRepo.getNotificationCount())
    }

}