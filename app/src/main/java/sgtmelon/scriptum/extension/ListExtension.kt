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

fun MutableList<RollItem>.getCheck(): Int {
    var rollCheck = 0
    this.forEach { if (it.isCheck) rollCheck++ }
    return rollCheck
}