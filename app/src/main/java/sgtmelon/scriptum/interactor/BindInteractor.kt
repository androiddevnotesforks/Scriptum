package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.interactor.callback.IBindInteractor

/**
 * Interactor for
 */
class BindInteractor(context: Context, private var callback: BindControl.NoteBridge.Notify?) :
        ParentInteractor(context),
        IBindInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

    /**
     * Update all bind notes in status bar rely on rank visibility
     */
    override suspend fun notifyBind() {
        val rankIdVisibleList = iRoomRepo.getRankIdVisibleList()

        iRoomRepo.getNoteModelList(bin = false).forEach {
            callback?.notifyBind(it, rankIdVisibleList)
        }
    }

}