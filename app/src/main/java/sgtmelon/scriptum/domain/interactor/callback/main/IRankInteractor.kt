package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.main.RankInteractor
import sgtmelon.scriptum.domain.model.item.RankItem

import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel

/**
 * Interface for communication [RankViewModel] with [RankInteractor]
 */
interface IRankInteractor : IParentInteractor {

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<RankItem>

    suspend fun getBind(noteId: List<Long>): Boolean


    suspend fun insert(name: String): RankItem

    suspend fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    suspend fun update(list: List<RankItem>)

    suspend fun updatePosition(list: List<RankItem>, noteIdList: List<Long>)

}