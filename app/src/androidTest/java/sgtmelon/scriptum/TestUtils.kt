package sgtmelon.scriptum

import java.util.*

object TestUtils {

    fun random(range: IntRange) =
            Random().nextInt((range.endInclusive + 1) - range.start) +  range.start

}