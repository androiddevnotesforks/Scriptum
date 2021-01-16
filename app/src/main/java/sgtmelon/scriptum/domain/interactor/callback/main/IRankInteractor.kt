package sgtmelon.scriptum.domain.interactor.callback.main

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.RankItem

import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel

/**
 * Interface for communication [IRankViewModel] with [RankInteractor].
 */
interface IRankInteractor : IParentInteractor {

    @Theme val theme: Int

    suspend fun getCount(): Int

    suspend fun getList(): MutableList<RankItem>

    suspend fun getBind(noteId: List<Long>): Boolean


    suspend fun insert(name: String): RankItem?

    suspend fun insert(item: RankItem)

    suspend fun delete(item: RankItem)

    suspend fun update(item: RankItem)

    suspend fun update(list: List<RankItem>)

    suspend fun updatePosition(list: List<RankItem>, idList: List<Long>)

}