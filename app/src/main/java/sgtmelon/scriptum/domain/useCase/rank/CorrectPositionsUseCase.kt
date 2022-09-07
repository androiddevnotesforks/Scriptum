package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

interface CorrectPositionsUseCase {

    /**
     * Return list of [NoteItem.id] where need to update [NoteItem.rankPs].
     */
    operator fun invoke(list: List<RankItem>): List<Long>
}