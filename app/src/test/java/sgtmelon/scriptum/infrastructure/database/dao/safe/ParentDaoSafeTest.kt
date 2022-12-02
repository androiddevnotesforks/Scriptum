package sgtmelon.scriptum.infrastructure.database.dao.safe

import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoConflictIdException
import sgtmelon.scriptum.infrastructure.model.exception.dao.DaoForeignException
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.OverflowDelegator
import sgtmelon.test.common.nextString

/**
 * Tests for ParentDaoSafe.
 */
class ParentDaoSafeTest : ParentTest() {

    private val overflowDelegator = OverflowDelegator(DaoConst.OVERFLOW_COUNT)

    @Test fun checkConflictSafe() {
        val long = Random.nextLong()

        FastMock.fireExtensions()
        every { any<DaoConflictIdException>().record() } returns mockk()

        assertEquals(long, long.checkConflictSafe())
        assertNull(DaoConst.UNIQUE_ERROR_ID.checkConflictSafe())
    }

    @Test fun insertForeignSafe() {
        val id = Random.nextLong()

        assertEquals(insertForeignSafe { id }, id)

        FastMock.fireExtensions()
        every { any<DaoForeignException>().record() } returns mockk()

        assertNull(insertForeignSafe { throw Throwable() })
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
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextBoolean() }

        var repeat = 0
        safeOverflow(list) {
            assertEquals(dividedList[repeat], it)
            repeat++
        }

        assertEquals(repeat, dividedList.size)
    }

    @Test fun `getSafeOverflowList lower overflow`() {
        val list = List((1 until DaoConst.OVERFLOW_COUNT).random()) { Random.nextInt() }

        var repeat = 0
        val resultList = getSafeOverflowList(list) {
            repeat++
            assertEquals(list, it)
            return@getSafeOverflowList it.filter { item -> item > 0 }
        }

        assertEquals(list.filter { it > 0 }, resultList)
        assertEquals(repeat, 1)
    }

    @Test fun `getSafeOverflowList equals overflow`() {
        val list = List(DaoConst.OVERFLOW_COUNT) { nextString() }

        var repeat = 0
        val resultList = getSafeOverflowList(list) {
            repeat++
            assertEquals(list, it)
            return@getSafeOverflowList it.map { item -> item.substring(0, 6) }
        }

        assertEquals(list.map { it.substring(0, 6) }, resultList)
        assertEquals(repeat, 1)
    }

    @Test fun `getSafeOverflowList greater overflow`() {
        val (list, dividedList) = overflowDelegator.getListPair { Random.nextBoolean() }

        var repeat = 0
        val resultList = getSafeOverflowList(list) {
            assertEquals(dividedList[repeat], it)
            repeat++
            return@getSafeOverflowList it.filter { item -> item }
        }

        assertEquals(list.filter { it }, resultList)
        assertEquals(repeat, dividedList.size)
    }
}