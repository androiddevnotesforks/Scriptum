package sgtmelon.scriptum.domain.useCase.note.createNote

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [CreateNoteUseCase].
 */
class CreateNoteUseCaseTest : ParentTest() {

    @MockK lateinit var createText: CreateTypeNoteUseCase<NoteItem.Text>
    @MockK lateinit var createRoll: CreateTypeNoteUseCase<NoteItem.Roll>

    private val useCase by lazy { CreateNoteUseCase(createText, createRoll) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(createText, createRoll)
    }

    @Test fun `invoke with text type`() {
        val item = mockk<NoteItem.Text>()

        every { createText() } returns item

        assertEquals(useCase(NoteType.TEXT), item)

        verifySequence {
            createText()
        }
    }

    @Test fun `invoke with roll type`() {
        val item = mockk<NoteItem.Roll>()

        every { createRoll() } returns item

        assertEquals(useCase(NoteType.ROLL), item)

        verifySequence {
            createRoll()
        }
    }
}