package sgtmelon.scriptum.extension

import sgtmelon.scriptum.model.item.RollItem

fun <T> MutableList<T>.swap(from: Int, to: Int) {
    val item = get(from)
    removeAt(from)
    add(to, item)
}

fun <T> MutableList<T>.clearAndAdd(replace: MutableList<T>) {
    clear()
    addAll(replace)
}

fun List<RollItem>.getCheck(): Int = filter { it.isCheck }.size

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }