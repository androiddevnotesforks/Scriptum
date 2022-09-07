package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

class CorrectPositionsUseCaseImpl : CorrectPositionsUseCase {

    override fun invoke(list: List<RankItem>): List<Long> {
        val noteIdSet = mutableSetOf<Long>()

        for ((i, item) in list.withIndex()) {
            /** If [RankItem.position] incorrect (out of order) when update it. */
            if (item.position != i) {
                item.position = i
                noteIdSet.addAll(item.noteId)
            }
        }

        return noteIdSet.toList()
    }
}