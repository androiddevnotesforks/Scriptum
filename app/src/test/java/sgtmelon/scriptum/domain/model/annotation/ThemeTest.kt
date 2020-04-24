package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Theme].
 */
class ThemeTest : ParentTest() {

    @Test fun todo() {
        assertEquals(-1, Theme.UNDEFINED)
        assertEquals(0, Theme.LIGHT)
        assertEquals(1, Theme.DARK)
    }

}