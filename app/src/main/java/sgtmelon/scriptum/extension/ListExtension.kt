package sgtmelon.scriptum.extension

import sgtmelon.scriptum.model.item.RollItem

/**
 * Move item by positions. If [to] is not indicated - move to last position
 */
fun <T> MutableList<T>.move(from: Int, to: Int = -1) {
    val item = removeAt(from)

    if (to == -1) add(item) else add(to, item)
}

fun <T> MutableList<T>.clearAndAdd(replace: List<T>) = apply {
    clear()
    addAll(replace)
}

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }