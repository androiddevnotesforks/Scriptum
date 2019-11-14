package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo

/**
 * Interactor for binding notification in status bar
 */
class BindInteractor(context: Context) : ParentInteractor(context),
        IBindInteractor {

    private val iBindRepo: IBindRepo = BindRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)

    /**
     * Update all bind notes in status bar rely on rank visibility
     */
    override fun notifyNoteBind(callback: BindControl.NoteBridge.Notify?) {
        val idVisibleList = iRankRepo.getIdVisibleList()

        iBindRepo.getNoteList().forEach {
            callback?.notifyNoteBind(it, idVisibleList)
        }
    }

    override fun notifyInfoBind(callback: BindControl.InfoBridge?) {
        callback?.notifyInfoBind(iBindRepo.getNotificationCount())
    }

}