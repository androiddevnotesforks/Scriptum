@file:JvmName(name = "ListExtensionUtils")

package sgtmelon.scriptum.cleanup.extension

import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull

//region Work with indexes

private const val ND_INDEX = -1

fun <T> List<T>.validIndexOfFirst(item: T): Int? {
    return indexOf(item).takeIf { it != ND_INDEX }
}

fun <T> List<T>.validIndexOfFirst(predicate: (T) -> Boolean): Int? {
    return indexOfFirst(predicate).takeIf { it != ND_INDEX }
}

//endregion

fun <T> MutableList<T>.move(from: Int, to: Int) {
    val item = removeAtOrNull(from) ?: return
    add(to, item)
}

fun <T> MutableList<T>.moveToEnd(from: Int) {
    val item = removeAtOrNull(from) ?: return
    add(item)
}