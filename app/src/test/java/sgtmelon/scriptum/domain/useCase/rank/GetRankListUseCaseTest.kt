package sgtmelon.scriptum.domain.useCase.rank

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [GetRankListUseCase].
 */
class GetRankListUseCaseTest : ParentTest() {

    @MockK lateinit var repository: RankRepo

    private val useCase by lazy { GetRankListUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val list = mockk<List<RankItem>>()

        coEvery { repository.getList() } returns list

        runBlocking {
            assertEquals(useCase(), list)
        }

        coVerifySequence {
            repository.getList()
        }
    }
}