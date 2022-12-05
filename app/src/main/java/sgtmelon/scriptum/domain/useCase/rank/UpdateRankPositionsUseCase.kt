package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

class UpdateRankPositionsUseCase(private val repository: RankRepo) {

    suspend operator fun invoke(list: List<RankItem>, noteIdList: List<Long>) {
        repository.updatePositions(list, noteIdList)
    }
}