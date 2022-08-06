package sgtmelon.scriptum.infrastructure.database.dao.safe

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
        val (list, dividedList) = OverflowDelegator.getListPair { Random.nextBoolean() }

        var repeat = 0
        safeOverflow(list) {
            assertEquals(dividedList[repeat], it)
            repeat++
        }

        assertEquals(repeat, dividedList.size)
    }
}