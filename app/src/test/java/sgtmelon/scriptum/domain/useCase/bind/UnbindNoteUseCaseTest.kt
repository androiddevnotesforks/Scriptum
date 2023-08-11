package sgtmelon.scriptum.domain.useCase.bind

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.data.repository.database.BindRepo
import kotlin.random.Random

/**
 * Test for [UnbindNoteUseCase].
 */
class UnbindNoteUseCaseTest : sgtmelon.tests.uniter.ParentTest() {

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