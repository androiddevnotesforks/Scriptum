package sgtmelon.scriptum.cleanup.domain.model.state

import android.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [ColorTransformation].
 */
class ColorTransformationTest : ParentTest() {

    @Test fun defaultValues() = with(ColorTransformation()) {
        assertEquals(ColorTransformation.ND_VALUE, from)
        assertEquals(ColorTransformation.ND_VALUE, to)
    }

    @Test fun isDifferent() {
        assertFalse(ColorTransformation().isReady())
        assertTrue(ColorTransformation(from = 1).isReady())
        assertTrue(ColorTransformation(to = 1).isReady())
        assertFalse(ColorTransformation(from = 1, to = 1).isReady())
    }

    @Test fun blend() {
        val from = Color.rgb(10, 10, 10)
        val to = Color.rgb(20, 20, 20)
        val colorTransformation = ColorTransformation(from, to)

        assertEquals(Color.rgb(13, 13, 13), colorTransformation.get(ratio = 0.3f))
        assertEquals(Color.rgb(15, 15, 15), colorTransformation.get(ratio = 0.5f))
        assertEquals(Color.rgb(17, 17, 17), colorTransformation.get(ratio = 0.7f))
    }
}