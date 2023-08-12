package sgtmelon.scriptum.domain.useCase.rank

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.test.common.getRandomSize
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [CorrectRankPositionsUseCase].
 */
class CorrectRankPositionsUseCaseTest : ParentTest() {

    private val useCase = CorrectRankPositionsUseCase()

    @Test fun `invoke shuffled`() {
        val listSize = getRandomSize()

        val positionList = List(listSize) { it }.shuffled()
        val noteIdList = List(listSize) {
            MutableList(getRandomSize()) { id -> ((it + 1) * id).toLong() }
        }
        val list = List(listSize) { mockk<RankItem>() }
        val resultIdSet = mutableSetOf<Long>()

        for (i in 0 until listSize) {
            every { list[i].position } returns positionList[i]

            if (i != positionList[i]) {
                every { list[i].position = i } returns Unit
                every { list[i].noteId } returns noteIdList[i]

                resultIdSet.addAll(noteIdList[i])
            }
        }

        assertEquals(useCase(list), resultIdSet.toList())

        verifySequence {
            for (i in 0 until listSize) {
                list[i].position
                if (i != positionList[i]) {
                    list[i].position = i
                    list[i].noteId
                }
            }
        }
    }

    @Test fun `invoke not shuffled`() {
        val listSize = getRandomSize()

        val positionList = List(listSize) { it }
        val list = List(listSize) { mockk<RankItem>() }

        for (i in 0 until listSize) {
            every { list[i].position } returns positionList[i]
        }

        assertEquals(useCase(list), listOf<Long>())

        verifySequence {
            for (i in 0 until listSize) {
                list[i].position
            }
        }
    }
}