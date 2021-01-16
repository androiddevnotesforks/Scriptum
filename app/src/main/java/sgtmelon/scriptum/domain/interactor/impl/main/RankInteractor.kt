package sgtmelon.scriptum.domain.interactor.impl.main

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.presentation.screen.vm.callback.main.IRankViewModel

/**
 * Interactor for [IRankViewModel].
 */
class RankInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val rankRepo: IRankRepo
) : ParentInteractor(), IRankInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme


    override suspend fun getCount(): Int = rankRepo.getCount()

    override suspend fun getList(): MutableList<RankItem> = rankRepo.getList()

    override suspend fun getBind(noteId: List<Long>): Boolean = rankRepo.getBind(noteId)


    override suspend fun insert(name: String): RankItem? {
        val id = rankRepo.insert(name) ?: return null

        return RankItem(id, name = name)
    }

    override suspend fun insert(item: RankItem) = rankRepo.insert(item)

    override suspend fun delete(item: RankItem) = rankRepo.delete(item)

    override suspend fun update(item: RankItem) = rankRepo.update(item)

    override suspend fun update(list: List<RankItem>) = rankRepo.update(list)

    override suspend fun updatePosition(list: List<RankItem>, idList: List<Long>) {
        rankRepo.updatePosition(list, idList)
    }

}