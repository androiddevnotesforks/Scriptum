package sgtmelon.scriptum.domain.useCase.note

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * Test fun [UpdateNoteUseCase].
 */
class UpdateNoteUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { UpdateNoteUseCase(repository) }

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
            repository.updateNote(item)
        }
    }
}