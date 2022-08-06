package sgtmelon.scriptum.infrastructure.database.dao.safe

import kotlin.math.ceil
import kotlin.math.min
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst

object OverflowDelegator {

    /**
     * Provides list for check overflow error (see description in [DaoConst.OVERFLOW_COUNT]).
     *
     * Returns full list and list with subList (converted from full list and divided
     * by [DaoConst.OVERFLOW_COUNT]).
     */
    inline fun <T> getListPair(
        expectedSize: Int? = null,
        getRandom: () -> T
    ): Pair<List<T>, List<List<T>>> {
        val size = expectedSize ?: run {
            val randomStart = DaoConst.OVERFLOW_COUNT + (1 until DaoConst.OVERFLOW_COUNT).random()
            val randomEnd = DaoConst.OVERFLOW_COUNT * (3..25).random()

            return@run (randomStart..randomEnd).random()
        }

        val list = List(size) { getRandom() }

        val sublistCount = ceil(x = list.size.toDouble() / DaoConst.OVERFLOW_COUNT).toInt()
        val dividedList = List(sublistCount) {
            val startIndex = it * DaoConst.OVERFLOW_COUNT
            val endIndex = min(a = startIndex + DaoConst.OVERFLOW_COUNT, list.size)
            return@List list.subList(startIndex, endIndex)
        }

        /**
         * Some assertions for make sure about list sizes.
         */
        for (divided in dividedList) {
            assertTrue(divided.size <= DaoConst.OVERFLOW_COUNT)
        }
        assertEquals(list.size, dividedList.sumOf { it.size })

        return list to dividedList
    }
}