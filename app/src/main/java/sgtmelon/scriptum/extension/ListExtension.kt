package sgtmelon.scriptum.extension

import sgtmelon.scriptum.model.item.RollItem

fun <T> MutableList<T>.removeAtOrNull(index: Int): T? {
    return if (index in 0..lastIndex) removeAt(index) else null
}

/**
 * Move item by positions. If [to] is not indicated - move to last position.
 */
fun <T> MutableList<T>.move(from: Int, to: Int = -1) {
    val item = removeAt(from)

    if (to == -1) add(item) else add(to, item)
}

fun <T> MutableList<T>.clearAddAll(replace: List<T>) = apply {
    clear()
    addAll(replace)
}

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }