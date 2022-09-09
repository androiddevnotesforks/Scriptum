package sgtmelon.scriptum.domain.useCase.main

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [ClearBinUseCase].
 */
class ClearBinUseCaseTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { ClearBinUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        runBlocking {
            useCase()
        }

        coVerifySequence {
            repository.clearBin()
        }
    }
}