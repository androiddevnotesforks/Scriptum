package sgtmelon.scriptum.domain.useCase.rank

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [DeleteRankUseCase].
 */
class DeleteRankUseCaseTest : ParentTest() {

    @MockK lateinit var repository: RankRepo

    private val useCase by lazy { DeleteRankUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val item = mockk<RankItem>()

        runBlocking {
            useCase(item)
        }

        coVerifySequence {
            repository.delete(item)
        }
    }
}