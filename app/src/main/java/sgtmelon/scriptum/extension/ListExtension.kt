package sgtmelon.scriptum.extension

import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem

fun <T> MutableList<T>.removeAtOrNull(index: Int): T? {
    return if (index in 0..lastIndex) removeAt(index) else null
}

private const val ND_INDEX = -1

fun <T> List<T>.correctIndexOf(item: T): Int? {
    return indexOf(item).takeIf { it != ND_INDEX }
}

fun <T> List<T>.correctIndexOfFirst(predicate: (T) -> Boolean): Int? {
    return indexOfFirst(predicate).takeIf { it != ND_INDEX }
}


/**
 * Move item by positions. If [to] position not defined, then move to last position.
 */
fun <T> MutableList<T>.move(from: Int, to: Int = ND_INDEX) {
    val item = removeAt(from)

    if (to == ND_INDEX) add(item) else add(to, item)
}

fun <T> MutableList<T>.clearAddAll(replace: List<T>) = apply {
    clear()
    addAll(replace)
}

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }

/**
 * Sort must be like in [INoteDao] queries.
 *
 * [1]  - Move to end of list;
 * [-1] - Move to start of list;
 * [0]  - Not move.
 */
fun List<NoteItem>.sort(@Sort sort: Int?): List<NoteItem> = let { list ->
    return@let when(sort) {
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
            it.create.getCalendar().timeInMillis
        })
        Sort.COLOR -> list.sortedWith(compareBy<NoteItem> {
            it.color
        }.thenByDescending {
            it.create.getCalendar().timeInMillis
        })
        else -> list
    }
}