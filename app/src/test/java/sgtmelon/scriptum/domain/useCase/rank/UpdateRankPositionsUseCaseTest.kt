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
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [UpdateRankPositionsUseCase].
 */
class UpdateRankPositionsUseCaseTest : ParentTest() {

    @MockK lateinit var repository: RankRepo

    private val useCase by lazy { UpdateRankPositionsUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val list = mockk<List<RankItem>>()
        val idList = mockk<List<Long>>()

        runBlocking {
            useCase(list, idList)
        }

        coVerifySequence {
            repository.updatePositions(list, idList)
        }
    }
}