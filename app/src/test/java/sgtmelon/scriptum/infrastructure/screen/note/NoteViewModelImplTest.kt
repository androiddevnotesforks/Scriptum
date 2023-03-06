package sgtmelon.scriptum.infrastructure.screen.note

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [NoteViewModelImpl].
 */
class NoteViewModelImplTest : ParentTest() {

    private val viewModel by lazy { NoteViewModelImpl() }

    @Test fun getDefaultColor() {
        TODO()
    }

    @Test fun `convertType from text`() {
        assertEquals(viewModel.convertType(NoteType.TEXT), NoteType.ROLL)
    }

    @Test fun `convertType from roll`() {
        assertEquals(viewModel.convertType(NoteType.ROLL), NoteType.TEXT)
    }
}