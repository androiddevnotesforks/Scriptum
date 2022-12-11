package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.infrastructure.database.DbData

class GetRankIdUseCase(private val repository: RankRepo) {

    /**
     * Return rank id related with [position].
     */
    suspend operator fun invoke(position: Int): Long {
        return if (position == DbData.Note.Default.RANK_PS) {
            DbData.Note.Default.RANK_ID
        } else {
            repository.getId(position) ?: DbData.Note.Default.RANK_ID
        }
    }
}