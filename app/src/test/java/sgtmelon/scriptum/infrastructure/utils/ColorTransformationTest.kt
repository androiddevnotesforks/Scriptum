package sgtmelon.scriptum.infrastructure.utils

import android.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test for [ColorTransformation].
 */
class ColorTransformationTest : sgtmelon.tests.uniter.ParentTest() {

    @Test fun defaultValues() = with(ColorTransformation()) {
        assertEquals(ColorTransformation.ND_VALUE, from)
        assertEquals(ColorTransformation.ND_VALUE, to)
    }

    @Test fun isReady() {
        assertFalse(ColorTransformation().isReady())
        assertTrue(ColorTransformation(from = 1).isReady())
        assertTrue(ColorTransformation(to = 1).isReady())
        assertFalse(ColorTransformation(from = 1, to = 1).isReady())
    }

    @Test fun transform() {
        val from = Color.rgb(10, 10, 10)
        val to = Color.rgb(20, 20, 20)
        val colorTransformation = ColorTransformation(from, to)

        assertEquals(Color.rgb(13, 13, 13), colorTransformation[0.3f])
        assertEquals(Color.rgb(15, 15, 15), colorTransformation[0.5f])
        assertEquals(Color.rgb(17, 17, 17), colorTransformation[0.7f])
    }
}