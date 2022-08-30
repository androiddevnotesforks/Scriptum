package sgtmelon.test.common

import java.util.UUID

fun nextString() = UUID.randomUUID().toString().substring(0, 16)

fun nextShortString() = nextString().substring(0, 4)

fun Int.isDivideEntirely(number: Int = 2): Boolean = this % number == 0

fun Long.isDivideEntirely(number: Long = 2): Boolean = this % number == 0L