package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Repeat].
 */
class RepeatTest : ParentTest() {

    @Test fun valueCheck() {
        assertEquals(0, Repeat.MIN_10)
        assertEquals(1, Repeat.MIN_30)
        assertEquals(2, Repeat.MIN_60)
        assertEquals(3, Repeat.MIN_180)
        assertEquals(4, Repeat.MIN_1440)
    }

}