@file:JvmName(name = "RollExtensionsUtils")

package sgtmelon.scriptum.infrastructure.utils.extensions.note

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

fun List<RollItem>.joinToText(): String = joinToString(separator = "\n") { it.text }

fun MutableList<RollItem>.copy() = map { it.copy() }.toMutableList()

fun List<RollItem>.hideChecked(): MutableList<RollItem> = ArrayList(filter { !it.isCheck })

fun List<RollItem>.getCheckCount(): Int = count { it.isCheck }

fun String.splitToRoll(): List<RollItem> {
    return split("\n".toRegex())
        .filter { it.isNotEmpty() }
        .mapIndexed { i, it -> RollItem(position = i, text = it) }
}