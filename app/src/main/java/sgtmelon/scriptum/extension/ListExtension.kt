package sgtmelon.scriptum.extension

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

fun MutableList<RollEntity>.getCheck(): Int {
    var rollCheck = 0
    this.forEach { if (it.isCheck) rollCheck++ }
    return rollCheck
}