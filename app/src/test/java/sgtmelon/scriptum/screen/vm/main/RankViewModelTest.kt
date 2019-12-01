package sgtmelon.scriptum.screen.vm.main

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.screen.vm.main.RankViewModel.Companion.correctPositions

/**
 * Test for [RankViewModel]
 */
class RankViewModelTest {

    @Test fun correctPositions() {
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

    private companion object {
        val rankFist = RankItem(
                id = 1, noteId = mutableListOf(1, 2), position = 0, name = "1", isVisible = true
        )
        val rankSecond = RankItem(
                id = 2, noteId = mutableListOf(2, 3), position = 1, name = "2", isVisible = false
        )
        val rankThird = RankItem(
                id = 3, noteId = mutableListOf(1, 5), position = 2, name = "3", isVisible = true
        )
        val rankFourth = RankItem(
                id = 4, noteId = mutableListOf(4, 6), position = 3, name = "4", isVisible = false
        )
    }

}