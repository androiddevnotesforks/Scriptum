package sgtmelon.scriptum.domain.useCase.system

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [UnbindNoteUseCase].
 */
class UnbindNoteUseCaseTest : ParentTest() {

    @MockK lateinit var repository: BindRepo

    private val useCase by lazy { UnbindNoteUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val noteId = Random.nextLong()

        coEvery { repository.unbindNote(noteId) } returns Unit

        runBlocking {
            useCase(noteId)
        }

        coVerifySequence {
            repository.unbindNote(noteId)
        }
    }
}