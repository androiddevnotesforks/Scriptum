package sgtmelon.extensions

import java.util.UUID

fun emptyString(): String = ""

val uniqueId: String get() = UUID.randomUUID().toString()

fun String.clearSplit(regex: Regex) = split(regex).dropLastWhile { it.isEmpty() }

fun String.clearSplit(regex: String) = clearSplit(regex.toRegex())

fun String.removeExtraSpace() = trim().replace("\\s+".toRegex(), replacement = " ")