package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo

/**
 * Interactor for binding notification in status bar
 */
class BindInteractor(context: Context) : ParentInteractor(context),
        IBindInteractor {

    private val iBindRepo: IBindRepo = BindRepo(context)
    private val iRankRepo: IRankRepo = RankRepo(context)
    private val iNoteRepo: INoteRepo = NoteRepo(context)

    /**
     * Update all bind notes in status bar rely on rank visibility
     */
    override suspend fun notifyNoteBind(callback: BindControl.NoteBridge.Notify?) {
        val rankIdVisibleList = iRankRepo.getIdVisibleList()

        iNoteRepo.getList(iPreferenceRepo.sort, bin = false, optimal = false).forEach {
            callback?.notifyNoteBind(it, rankIdVisibleList)
        }
    }

    override suspend fun notifyInfoBind(callback: BindControl.InfoBridge?) {
        callback?.notifyInfoBind(iBindRepo.getNotificationCount())
    }

}