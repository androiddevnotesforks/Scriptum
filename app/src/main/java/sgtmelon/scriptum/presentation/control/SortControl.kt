package sgtmelon.scriptum.presentation.control

import sgtmelon.common.getCalendar
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem

/**
 * Class for sorting lists
 */
object SortControl {

    /**
     * Sort must be like in [INoteDao] queries.
     *
     * [1]  - Move to end of list;
     * [-1] - Move to start of list;
     * [0]  - Not move.
     */
    suspend fun sortList(list: List<NoteItem>, @Sort sort: Int): List<NoteItem> = when (sort) {
        Sort.CHANGE -> list.sortedByDescending { it.change.getCalendar().timeInMillis }
        Sort.CREATE -> list.sortedByDescending { it.create.getCalendar().timeInMillis }
        Sort.RANK -> list.sortedWith(Comparator<NoteItem> { o1, o2 ->
            return@Comparator when {
                !o1.haveRank() && o2.haveRank() -> 1
                o1.haveRank() && !o2.haveRank() -> -1
                o1.rankPs > o2.rankPs -> 1
                o1.rankPs < o2.rankPs -> -1
                else -> 0
            }
        }.thenByDescending {
            it.change.getCalendar().timeInMillis
        })
        Sort.COLOR -> list.sortedWith(compareBy<NoteItem> {
            it.color
        }.thenByDescending {
            it.change.getCalendar().timeInMillis
        })
        else -> list
    }

}