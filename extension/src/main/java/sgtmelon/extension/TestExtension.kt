package sgtmelon.extension

import java.util.*

fun nextString() = UUID.randomUUID().toString().substring(0, 16)

fun nextShortString() = nextString().substring(0, 4)