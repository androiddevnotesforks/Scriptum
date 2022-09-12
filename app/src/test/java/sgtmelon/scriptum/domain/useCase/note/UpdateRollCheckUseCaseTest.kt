package sgtmelon.scriptum.domain.useCase.note

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [UpdateRollCheckUseCase].
 */
class UpdateRollCheckUseCaseTest : ParentTest() {

    @MockK lateinit var repository: NoteRepo

    private val useCase by lazy { UpdateRollCheckUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val item = mockk<NoteItem.Roll>()
        val p = Random.nextInt()

        runBlocking {
            useCase(item, p)
        }

        coVerifySequence {
            repository.updateRollCheck(item, p)
        }
    }
}