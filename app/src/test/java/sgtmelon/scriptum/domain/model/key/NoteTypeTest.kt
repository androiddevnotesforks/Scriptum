package sgtmelon.scriptum.domain.model.key

import org.junit.Assert
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [NoteType].
 */
class NoteTypeTest : ParentTest() {

    @Test fun position() {
        Assert.assertEquals(0, NoteType.TEXT.ordinal)
        Assert.assertEquals(1, NoteType.ROLL.ordinal)
    }

}