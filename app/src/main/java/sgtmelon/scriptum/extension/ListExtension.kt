@file:JvmName(name = "ListExtensionUtils")

package sgtmelon.scriptum.extension

import sgtmelon.scriptum.domain.model.item.RollItem

fun <T> MutableList<T>.removeAtOrNull(index: Int): T? {
    return if (index in indices) removeAt(index) else null
}

//region Work with indexes

private const val ND_INDEX = -1

fun <T> List<T>.indexOfOrNull(item: T): Int? {
    return indexOf(item).takeIf { it != ND_INDEX }
}

fun <T> List<T>.indexOfOrNull(predicate: (T) -> Boolean): Int? {
    return indexOfFirst(predicate).takeIf { it != ND_INDEX }
}

//endregion

/**
 * Move item by positions. If [to] position not defined, then move to last position.
 */
fun <T> MutableList<T>.move(from: Int, to: Int = ND_INDEX) {
    val item = removeAt(from)

    if (to == ND_INDEX) add(item) else add(to, item)
}

fun <T> MutableList<T>.clearAdd(replace: List<T>) = apply {
    clear()
    addAll(replace)
}

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }

fun MutableList<RollItem>.copy() = map { it.copy() }.toMutableList()

fun BooleanArray.safeSet(index: Int, value: Boolean) {
    if (index in indices) set(index, value)
}