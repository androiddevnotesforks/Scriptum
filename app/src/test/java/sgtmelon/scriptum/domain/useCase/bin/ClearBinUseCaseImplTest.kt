package sgtmelon.scriptum.domain.useCase.bin

import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [ClearBinUseCaseImpl].
 */
class ClearBinUseCaseImplTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { ClearBinUseCaseImpl(repository) }

    @After override fun tearDown() {
        super.tearDown()
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