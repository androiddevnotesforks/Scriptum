package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Interactor for binding notification in status bar.
 */
class BindInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val bindRepo: IBindRepo,
        private val rankRepo: IRankRepo,
        private val noteRepo: INoteRepo
) : ParentInteractor(),
        IBindInteractor {

    /**
     * Update all bind notes in status bar rely on rank visibility.
     */
    override suspend fun notifyNoteBind(callback: BindControl.NoteBridge.NotifyAll?) {
        if (callback == null) return

        val sort = preferenceRepo.sort ?: return
        val rankIdVisibleList = rankRepo.getIdVisibleList() ?: return
        val itemList = noteRepo.getList(sort, bin = false, isOptimal = false, filterVisible = false)
                ?: return

        callback.notifyNoteBind(itemList, rankIdVisibleList)
    }

    override suspend fun notifyInfoBind(callback: BindControl.InfoBridge?) {
        if (callback == null) return

        val count = bindRepo.getNotificationCount() ?: return
        callback.notifyInfoBind(count)
    }

}