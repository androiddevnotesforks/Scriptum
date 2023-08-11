package sgtmelon.scriptum.domain.useCase.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Test for [ConvertNoteUseCase].
 */
class ConvertNoteUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { ConvertNoteUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun `invoke with text`() {
        val item = mockk<NoteItem.Text>()
        val newItem = mockk<NoteItem.Roll>()

        coEvery { repository.convertNote(item) } returns newItem

        runBlocking {
            assertEquals(useCase(item), newItem)
            assertEquals(useCase(item as NoteItem), newItem)
        }

        coVerifySequence {
            repository.convertNote(item)
            repository.convertNote(item)
        }
    }

    @Test fun `invoke with roll`() {
        val item = mockk<NoteItem.Roll>()
        val newItem = mockk<NoteItem.Text>()

        coEvery { repository.convertNote(item) } returns newItem

        runBlocking {
            assertEquals(useCase(item), newItem)
            assertEquals(useCase(item as NoteItem), newItem)
        }

        coVerifySequence {
            repository.convertNote(item)
            repository.convertNote(item)
        }
    }
}