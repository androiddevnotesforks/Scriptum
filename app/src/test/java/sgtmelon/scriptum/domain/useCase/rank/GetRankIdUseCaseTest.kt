package sgtmelon.scriptum.domain.useCase.rank

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.infrastructure.database.DbData
import sgtmelon.tests.uniter.ParentTest
import kotlin.random.Random

/**
 * Test for [GetRankIdUseCase].
 */
class GetRankIdUseCaseTest : ParentTest() {

    @MockK lateinit var repository: RankRepo

    private val useCase by lazy { GetRankIdUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val defaultId = DbData.Note.Default.RANK_ID
        val id = Random.nextLong()

        val defaultPosition = DbData.Note.Default.RANK_PS
        val position = Random.nextInt()

        runBlocking {
            assertEquals(useCase(defaultPosition), defaultId)
        }

        coEvery { repository.getId(position) } returns null

        runBlocking {
            assertEquals(useCase(position), defaultId)
        }

        coEvery { repository.getId(position) } returns id

        runBlocking {
            assertEquals(useCase(position), id)
        }

        coVerifySequence {
            repository.getId(position)
            repository.getId(position)
        }
    }
}