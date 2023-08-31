package sgtmelon.extensions

fun Int.toHexString() = String.format("#%06X", 0xFFFFFF and this)