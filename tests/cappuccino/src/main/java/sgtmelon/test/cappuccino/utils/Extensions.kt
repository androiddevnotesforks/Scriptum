package sgtmelon.test.cappuccino.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun await(time: Long) = runBlocking { delay(time) }
