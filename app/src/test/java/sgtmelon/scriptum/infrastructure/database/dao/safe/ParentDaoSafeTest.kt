package sgtmelon.scriptum.infrastructure.database.dao.safe

import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst

/**
 * Tests for ParentDaoSafe.
 */
class ParentDaoSafeTest : ParentTest() {

    @Test fun checkSafe() {
        val long = Random.nextLong()

        assertEquals(long, long.checkSafe())
        assertNull(DaoConst.UNIQUE_ERROR_ID.checkSafe())
    }

    @Test fun `safeOverflow lower overflow`() {
        val list = List((1 until DaoConst.OVERFLOW_COUNT).random()) { Random.nextInt() }

        var repeat = 0
        safeOverflow(list) {
            assertEquals(list, it)
            repeat++
        }

        assertEquals(repeat, 1)
    }

    @Test fun `safeOverflow equals overflow`() {
        val list = List(DaoConst.OVERFLOW_COUNT) { nextString() }

        var repeat = 0
        safeOverflow(list) {
            assertEquals(list, it)
            repeat++
        }

        assertEquals(repeat, 1)
    }

    @Test fun `safeOverflow greater overflow`() {
        val randomStart = DaoConst.OVERFLOW_COUNT + (1 until DaoConst.OVERFLOW_COUNT).random()
        val randomEnd = DaoConst.OVERFLOW_COUNT * (3..100).random()
        val list = List((randomStart..randomEnd).random()) { Random.nextBoolean() }

        val sublistCount = ceil(x = list.size.toDouble() / DaoConst.OVERFLOW_COUNT).toInt()
        val sublistList = List(sublistCount) {
            val startIndex = it * DaoConst.OVERFLOW_COUNT
            val endIndex = min(a = startIndex + DaoConst.OVERFLOW_COUNT, list.size)
            return@List list.subList(startIndex, endIndex)
        }

        var repeat = 0
        safeOverflow(list) {
            assertEquals(sublistList[repeat], it)
            repeat++
        }

        assertEquals(repeat, sublistList.size)
    }
}