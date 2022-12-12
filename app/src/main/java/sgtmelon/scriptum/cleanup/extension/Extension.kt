package sgtmelon.scriptum.cleanup.extension

fun String.clearSpace() = trim().replace("\\s+".toRegex(), replacement = " ")