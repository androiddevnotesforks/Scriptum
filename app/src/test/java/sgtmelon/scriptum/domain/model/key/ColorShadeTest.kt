package sgtmelon.scriptum.domain.model.key

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [ColorShade].
 */
class ColorShadeTest : ParentTest() {

    @Test fun position() {
        assertEquals(0, ColorShade.LIGHT.ordinal)
        assertEquals(1, ColorShade.ACCENT.ordinal)
        assertEquals(2, ColorShade.DARK.ordinal)
    }

}