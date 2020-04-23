package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [InputAction].
 */
class InputActionTest : ParentTest() {

    @Test fun valueCheck() {
        assertEquals(0, InputAction.RANK)
        assertEquals(1, InputAction.COLOR)
        assertEquals(2, InputAction.NAME)
        assertEquals(3, InputAction.TEXT)
        assertEquals(4, InputAction.ROLL)
        assertEquals(5, InputAction.ROLL_ADD)
        assertEquals(6, InputAction.ROLL_REMOVE)
        assertEquals(7, InputAction.ROLL_MOVE)
    }

}