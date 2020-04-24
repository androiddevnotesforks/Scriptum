package sgtmelon.scriptum.domain.model.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [Options].
 */
class OptionsTest : ParentTest() {

    @Test fun valueNotesCheck() {
        assertEquals(0, Options.Notes.NOTIFICATION)
        assertEquals(1, Options.Notes.BIND)
        assertEquals(2, Options.Notes.CONVERT)
        assertEquals(3, Options.Notes.COPY)
        assertEquals(4, Options.Notes.DELETE)
    }

    @Test fun valueBinCheck() {
        assertEquals(0, Options.Bin.RESTORE)
        assertEquals(1, Options.Bin.COPY)
        assertEquals(2, Options.Bin.CLEAR)
    }

}