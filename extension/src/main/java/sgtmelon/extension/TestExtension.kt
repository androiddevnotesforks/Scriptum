package sgtmelon.extension

import java.util.*
import kotlin.random.Random

fun Random.nextString() = UUID.randomUUID().toString().substring(0, 16)