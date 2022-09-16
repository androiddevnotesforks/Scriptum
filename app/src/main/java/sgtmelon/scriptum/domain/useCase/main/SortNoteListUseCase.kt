package sgtmelon.scriptum.domain.useCase.main

import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.Sort

class SortNoteListUseCase {

    /**
     * Sort must be like in dao queries.
     *
     * [1]  - Move to end of list;
     * [-1] - Move to start of list;
     * [0]  - Not move.
     */
    suspend operator fun invoke(list: List<NoteItem>, sort: Sort): List<NoteItem> {
        return when (sort) {
            Sort.CHANGE -> sortByChange(list)
            Sort.CREATE -> sortByCreate(list)
            Sort.RANK -> sortByRank(list)
            Sort.COLOR -> sortByColor(list)
        }
    }

    private fun sortByChange(list: List<NoteItem>): List<NoteItem> {
        return list.sortedByDescending { it.change.toCalendar().timeInMillis }
    }

    private fun sortByCreate(list: List<NoteItem>): List<NoteItem> {
        return list.sortedByDescending { it.create.toCalendar().timeInMillis }
    }

    private fun sortByRank(list: List<NoteItem>): List<NoteItem> {
        return list.sortedWith(Comparator<NoteItem> { o1, o2 ->
            return@Comparator when {
                !o1.haveRank() && o2.haveRank() -> 1
                o1.haveRank() && !o2.haveRank() -> -1
                o1.rankPs > o2.rankPs -> 1
                o1.rankPs < o2.rankPs -> -1
                else -> 0
            }
        }.thenByDescending { it.change.toCalendar().timeInMillis })
    }

    private fun sortByColor(list: List<NoteItem>): List<NoteItem> {
        return list.sortedWith(compareBy<NoteItem> { it.color }
            .thenByDescending { it.change.toCalendar().timeInMillis })
    }

}