package sgtmelon.scriptum.cleanup.domain.model.state

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [NoteState].
 */
class NoteStateTest : ParentTest() {

    private val noteState = NoteState()

    @Test fun defaultValues() {
        assertEquals(NoteState.ND_CREATE, noteState.isCreate)
        assertEquals(NoteState.ND_BIN, noteState.isBin)

        assertEquals(noteState.isEdit, noteState.isEdit)
    }
}