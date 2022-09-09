package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

class InsertRankUseCase(private val repository: RankRepo) {

    suspend operator fun invoke(name: String): RankItem? = repository.insert(name)

    suspend operator fun invoke(item: RankItem) = repository.insert(item)
}