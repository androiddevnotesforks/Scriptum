package sgtmelon.scriptum.extension

import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.room.entity.RollEntity

fun <T> MutableList<T>.swap(from: Int, to: Int) {
    val item = get(from)
    removeAt(from)
    add(to, item)
}

fun <T> MutableList<T>.clearAndAdd(replace: MutableList<T>) {
    clear()
    addAll(replace)
}

fun List<RollEntity>.getCheck(): Int {
    var rollCheck = 0
    this.forEach { if (it.isCheck) rollCheck++ }
    return rollCheck
}

fun List<RollItem>.getCheck2(): Int = filter { it.isCheck }.size

fun List<RollItem>.getText(): String = joinToString(separator = "\n") { it.text }