package sgtmelon.scriptum.interactor.main

import android.content.Context

import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.screen.ui.callback.main.IRankBridge
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Interactor for [RankViewModel]
 */
class RankInteractor(context: Context, private var callback: IRankBridge?) :
        ParentInteractor(context),
        IRankInteractor {

    private val iRankRepo: IRankRepo = RankRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    /**
     * Обновление по категориям всех прикреплённых заметок в статус баре
     */
    override fun notifyBind() {
        val rankIdVisibleList = iRoomRepo.getRankIdVisibleList()

        iRoomRepo.getNoteModelList(bin = false).forEach {
            callback?.notifyBind(it, rankIdVisibleList)
        }
    }

    override fun insert(name: String) = RankEntity(name = name).apply {
        id = iRankRepo.insert(rankEntity = this)
    }

    override fun getList() = iRankRepo.getList()

    override fun delete(rankEntity: RankEntity) = iRankRepo.delete(rankEntity)

    override suspend fun update(rankEntity: RankEntity) = iRankRepo.update(rankEntity)

    override fun update(list: List<RankEntity>) = iRankRepo.update(list)

}