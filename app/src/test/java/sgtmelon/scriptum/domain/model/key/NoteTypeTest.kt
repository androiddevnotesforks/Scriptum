package sgtmelon.scriptum.domain.model.key

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [NoteType].
 */
class NoteTypeTest : ParentTest() {

    @Test fun position() {
        assertEquals(0, NoteType.TEXT.ordinal)
        assertEquals(1, NoteType.ROLL.ordinal)
    }

}