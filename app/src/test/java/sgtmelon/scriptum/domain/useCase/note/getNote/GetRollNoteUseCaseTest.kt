package sgtmelon.scriptum.domain.useCase.note.getNote

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.exception.note.IllegalNoteTypeException
import sgtmelon.scriptum.infrastructure.utils.extensions.record
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [GetRollNoteUseCase].
 */
class GetRollNoteUseCaseTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { GetRollNoteUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val noteId = Random.nextLong()
        val item = mockk<NoteItem.Roll>()

        coEvery { repository.getItem(noteId, isOptimal = false) } returns item
        runBlocking {
            assertEquals(item, useCase(noteId))
        }

        coVerifySequence {
            repository.getItem(noteId, isOptimal = false)
        }
    }

    @Test fun `invoke with bad result`() {
        val noteId = Random.nextLong()
        val wrongItem = mockk<NoteItem.Text>()

        FastMock.fireExtensions()
        every { any<IllegalNoteTypeException>().record() } returns mockk()

        coEvery { repository.getItem(noteId, isOptimal = false) } returns null
        runBlocking {
            assertNull(useCase(noteId))
        }

        coEvery { repository.getItem(noteId, isOptimal = false) } returns wrongItem
        runBlocking {
            assertNull(useCase(noteId))
        }

        coVerifySequence {
            repository.getItem(noteId, isOptimal = false)
            any<IllegalNoteTypeException>().record()
            repository.getItem(noteId, isOptimal = false)
            any<IllegalNoteTypeException>().record()
        }
    }
}