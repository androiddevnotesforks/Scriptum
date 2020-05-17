package sgtmelon.scriptum.domain.model.key

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Complete].
 */
class CompleteTest : ParentTest() {

    @Test fun position() {
        assertEquals(0, Complete.EMPTY.ordinal)
        assertEquals(1, Complete.FULL.ordinal)
    }

}