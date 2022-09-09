package sgtmelon.scriptum.domain.useCase.rank

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem

class CorrectPositionsUseCase {

    /**
     * Return list of [NoteItem.id] where need to update [NoteItem.rankPs].
     */
    operator fun invoke(list: List<RankItem>): List<Long> {
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