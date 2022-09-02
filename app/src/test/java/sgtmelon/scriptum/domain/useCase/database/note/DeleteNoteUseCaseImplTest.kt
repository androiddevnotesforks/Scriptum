package sgtmelon.scriptum.domain.useCase.database.note

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [DeleteNoteUseCaseImpl].
 */
class DeleteNoteUseCaseImplTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { DeleteNoteUseCaseImpl(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val item = mockk<NoteItem>()

        runBlocking {
            useCase(item)
        }

        coVerifySequence {
            repository.deleteNote(item)
        }
    }
}