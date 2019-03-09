package sgtmelon.scriptum

import java.util.*

object TestUtils {

    fun IntRange.random() =
            if (!isEmpty()) Random().nextInt((endInclusive + 1) - start) + start else 0

}