package sgtmelon.scriptum.cleanup.presentation.control

import sgtmelon.common.utils.getCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.Sort

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
    // TODO move inside useCase
    suspend fun sortList(list: List<NoteItem>, sort: Sort): List<NoteItem> = when (sort) {
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
    }

}