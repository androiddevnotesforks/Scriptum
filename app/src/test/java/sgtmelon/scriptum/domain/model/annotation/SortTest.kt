package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Sort].
 */
class SortTest : ParentTest() {

    @Test fun valueCheck() {
        assertEquals(0, Sort.CHANGE)
        assertEquals(1, Sort.CREATE)
        assertEquals(2, Sort.RANK)
        assertEquals(3, Sort.COLOR)
    }

}