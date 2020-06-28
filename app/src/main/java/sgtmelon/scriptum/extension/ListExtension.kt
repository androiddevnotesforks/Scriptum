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

fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    return indexOfFirst(predicate).takeIf { it != ND_INDEX }
}

/**
 * Func like [String.substringAfter] but on fail returns null.
 */
fun String.substringAfterOrNull(delimiter: String): String? {
    val index = indexOf(delimiter)
    return if (index != ND_INDEX) substring(index + delimiter.length, length) else null
}

/**
 * Func like [String.substringAfterLast] but on fail returns null.
 */
fun String.substringAfterLastOrNull(delimiter: String) : String? {
    val index = lastIndexOf(delimiter)
    return if (index != ND_INDEX) substring(index + 1, length) else null
}

/**
 * Func like [String.substringBefore] but on fail returns null.
 */
fun String.substringBeforeOrNull(delimiter: String): String? {
    val index = indexOf(delimiter)
    return if (index != ND_INDEX) substring(0, index) else null
}

/**
 * Func like [String.substringBeforeLast] but on fail returns null.
 */
fun String.substringBeforeLastOrNull(delimiter: String): String? {
    val index = lastIndexOf(delimiter)
    return if (index != ND_INDEX) substring(0, index) else null
}

/**
 * Return string which placed after [first] delimiter end before [last] delimiter.
 *
 * Note: [last] delimiter takes from end of the string.
 */
fun String.substringBetweenOrNull(first: String, last: String): String? {
    return substringAfterOrNull(first)?.substringBeforeLastOrNull(last)
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