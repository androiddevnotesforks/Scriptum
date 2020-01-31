package sgtmelon.scriptum.screen.vm.main

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.vm.main.RankViewModel.Companion.correctPositions

/**
 * Test for [RankViewModel]
 */
@ExperimentalCoroutinesApi
class RankViewModelTest : ParentTest() {

    private val data = TestData.Rank

    @Test fun correctPositions() = with(data) {
        var list = listOf(rankFist.copy(), rankSecond.copy(), rankThird.copy(), rankFourth.copy())
        var noteIdList = list.correctPositions()

        assertTrue(noteIdList.isEmpty())
        assertPositions(list)

        list = listOf(rankSecond.copy(), rankThird.copy(), rankFist.copy(), rankFourth.copy())
        noteIdList = list.correctPositions()

        assertEquals(listOf(2L, 3, 1, 5), noteIdList)
        assertPositions(list)

        list = listOf(rankFourth.copy(), rankSecond.copy(), rankFist.copy(), rankThird.copy())
        noteIdList = list.correctPositions()

        assertEquals(listOf(4L, 6, 1, 2, 5), noteIdList)
        assertPositions(list)
    }

    private fun assertPositions(list: List<RankItem>) = list.forEachIndexed { i, item ->
        assertEquals(i, item.position)
    }

}