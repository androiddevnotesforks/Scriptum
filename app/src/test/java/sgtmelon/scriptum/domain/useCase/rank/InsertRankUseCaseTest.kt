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
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [InsertRankUseCase].
 */
class InsertRankUseCaseTest : ParentTest() {

    @MockK lateinit var repository: RankRepo

    private val useCase by lazy { InsertRankUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun `invoke with name`() {
        val name = nextString()
        val item = mockk<RankItem>()

        coEvery { repository.insert(name) } returns item

        runBlocking {
            assertEquals(useCase(name), item)
        }

        coVerifySequence {
            repository.insert(name)
        }
    }

    @Test fun `invoke with item`() {
        val item = mockk<RankItem>()

        runBlocking {
            useCase(item)
        }

        coVerifySequence {
            repository.insert(item)
        }
    }
}