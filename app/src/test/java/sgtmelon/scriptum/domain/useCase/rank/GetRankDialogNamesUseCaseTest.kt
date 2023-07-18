package sgtmelon.scriptum.domain.useCase.rank

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.getRandomSize
import sgtmelon.test.common.nextString

/**
 * Test for [GetRankDialogNamesUseCase].
 */
class GetRankDialogNamesUseCaseTest : ParentTest() {

    private val name = nextString()
    @MockK lateinit var repository: RankRepo

    private val useCase by lazy { GetRankDialogNamesUseCase(name, repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        val nameList = List(getRandomSize()) { nextString() }
        val resultArray = mutableListOf(name).apply { addAll(nameList) }.toTypedArray()

        coEvery { repository.getNameList() } returns nameList

        runBlocking {
            assertArrayEquals(useCase(), resultArray)
        }

        coVerifySequence {
            repository.getNameList()
        }
    }
}