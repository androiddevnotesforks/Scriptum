@file:OptIn(ExperimentalCoroutinesApi::class)

package sgtmelon.scriptum.domain.useCase.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [GetNoteUseCase].
 */
class GetNoteUseCaseTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { GetNoteUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() = runTest {
        val noteId = Random.nextLong()
        val item = mockk<NoteItem>()

        coEvery { repository.getItem(noteId) } returns null
        assertNull(useCase(noteId))

        coEvery { repository.getItem(noteId) } returns item
        assertEquals(useCase(noteId), item)

        coVerifySequence {
            repository.getItem(noteId)
            repository.getItem(noteId)
        }
    }
}