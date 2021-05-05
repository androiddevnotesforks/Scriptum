package sgtmelon.scriptum.domain.model.state

import android.graphics.Color
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [MenuColorState].
 */
class MenuColorStateTest : ParentTest() {

    @Test fun defaultValues() = with(MenuColorState()) {
        assertEquals(MenuColorState.ND_VALUE, from)
        assertEquals(MenuColorState.ND_VALUE, to)
    }

    @Test fun isDifferent() {
        assertFalse(MenuColorState().isDifferent())
        assertTrue(MenuColorState(from = 1).isDifferent())
        assertTrue(MenuColorState(to = 1).isDifferent())
        assertFalse(MenuColorState(from = 1, to = 1).isDifferent())
    }

    @Test fun blend() {
        val from = Color.rgb(10, 10, 10)
        val to = Color.rgb(20, 20, 20)
        val menuColorState = MenuColorState(from, to)

        assertEquals(Color.rgb(13,13,13), menuColorState.blend(ratio = 0.3f))
        assertEquals(Color.rgb(15,15,15), menuColorState.blend(ratio = 0.5f))
        assertEquals(Color.rgb(17,17,17), menuColorState.blend(ratio = 0.7f))
    }
}