package sgtmelon.test.common

import kotlin.math.ceil
import kotlin.math.min
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Provides list for check overflow error.
 *
 * Returns full list and list with subLists (converted from full list and
 * divided by [overflowCount]).
 */
class OverflowDelegator(private val overflowCount: Int) {


    fun <T> getListPair(
        expectedSize: Int? = null,
        getRandom: (iteration: Int) -> T
    ): Pair<List<T>, List<List<T>>> {
        val list = getList(expectedSize, getRandom)

        val sublistCount = ceil(x = list.size.toDouble() / overflowCount).toInt()
        val dividedList = List(sublistCount) {
            val startIndex = it * overflowCount
            val endIndex = min(a = startIndex + overflowCount, list.size)
            return@List list.subList(startIndex, endIndex)
        }

        /**
         * Some assertions for make sure about list sizes.
         */
        for (divided in dividedList) {
            assertTrue(divided.size <= overflowCount)
        }
        assertEquals(list.size, dividedList.sumOf { it.size })

        return list to dividedList
    }

    fun <T> getList(
        expectedSize: Int? = null,
        getRandom: (iteration: Int) -> T
    ): List<T> {
        val size = expectedSize ?: run {
            val randomStart = overflowCount + (1 until overflowCount).random()
            val randomEnd = overflowCount * (3..10).random()

            return@run (randomStart..randomEnd).random()
        }

        return List(size) { getRandom(it) }
    }
}