@file:JvmName(name = "ListExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils.extensions


fun <T> MutableList<T>.removeAtOrNull(index: Int): T? {
    return if (index in indices) removeAt(index) else null
}

fun <T> MutableList<T>.clearAdd(replace: List<T>) = apply {
    clear()
    addAll(replace)
}