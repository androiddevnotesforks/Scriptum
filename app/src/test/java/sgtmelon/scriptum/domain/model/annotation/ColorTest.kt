package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Color].
 */
class ColorTest : ParentTest() {

    @Test fun valueCheck() {
        assertEquals(0, Color.RED)
        assertEquals(1, Color.PURPLE)
        assertEquals(2, Color.INDIGO)
        assertEquals(3, Color.BLUE)
        assertEquals(4, Color.TEAL)
        assertEquals(5, Color.GREEN)
        assertEquals(6, Color.YELLOW)
        assertEquals(7, Color.ORANGE)
        assertEquals(8, Color.BROWN)
        assertEquals(9, Color.BLUE_GREY)
        assertEquals(10, Color.WHITE)
    }

    @Test fun listOrder() = Color.list.forEachIndexed { i, color -> assertEquals(i, color) }

}