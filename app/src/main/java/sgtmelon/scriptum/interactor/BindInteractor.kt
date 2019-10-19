package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo

/**
 * Interactor for
 */
class BindInteractor(context: Context, private var callback: BindControl.NoteBridge.Notify?) :
        ParentInteractor(context),
        IBindInteractor {

    private val iBindRepo: IBindRepo = BindRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

    /**
     * Update all bind notes in status bar rely on rank visibility
     */
    override fun notifyBind() {
        val rankIdVisibleList = iRoomRepo.getRankIdVisibleList()

        iBindRepo.getNoteList().forEach {
            callback?.notifyBind(it, rankIdVisibleList)
        }
    }

}