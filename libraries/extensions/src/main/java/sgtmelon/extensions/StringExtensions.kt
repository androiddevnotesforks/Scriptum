package sgtmelon.extensions

fun String.clearSplit(regex: Regex) = split(regex).dropLastWhile { it.isEmpty() }

fun String.clearSplit(regex: String) = clearSplit(regex.toRegex())

fun String.removeExtraSpace() = trim().replace("\\s+".toRegex(), replacement = " ")