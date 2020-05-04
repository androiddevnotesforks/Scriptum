package sgtmelon.scriptum.domain.model.key

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [PermissionResult].
 */
class PermissionResultTest : ParentTest() {

    @Test fun position() {
        assertEquals(0, PermissionResult.LOW_API.ordinal)
        assertEquals(1, PermissionResult.ALLOWED.ordinal)
        assertEquals(2, PermissionResult.FORBIDDEN.ordinal)
        assertEquals(3, PermissionResult.GRANTED.ordinal)
    }

}