package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

class GetRankListUseCase(private val repository: RankRepo) {

    suspend operator fun invoke(): List<RankItem> = repository.getList()
}