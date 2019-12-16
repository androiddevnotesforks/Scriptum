package sgtmelon.scriptum.screen.vm.main

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.vm.main.NotesViewModel.Companion.sort

/**
 * Test for [NotesViewModel].
 */
class NotesViewModelTest {

    @Test fun sort() {
        assertEquals(changeList, unsortedList.sort(Sort.CHANGE))
        assertEquals(createList, unsortedList.sort(Sort.CREATE))
        assertEquals(rankList, unsortedList.sort(Sort.RANK))
        assertEquals(colorList, unsortedList.sort(Sort.COLOR))
    }


    private companion object {
        const val DATE_0 = "1234-01-02 03:04:05"
        const val DATE_1 = "1345-02-03 04:05:06"
        const val DATE_2 = "1456-03-04 05:06:07"
        const val DATE_3 = "1567-04-05 06:07:08"

        val noteFirst = NoteItem(
                id = 0, create = DATE_1, change = DATE_2, color = 0, type = NoteType.TEXT,
                rankId = -1, rankPs = -1
        )

        val noteSecond = NoteItem(
                id = 1, create = DATE_0, change = DATE_3, color = 2, type = NoteType.TEXT,
                rankId = 1, rankPs = 1
        )

        val noteThird = NoteItem(
                id = 2, create = DATE_3, change = DATE_0, color = 4, type = NoteType.TEXT,
                rankId = 1, rankPs = 1
        )

        val noteFourth = NoteItem(
                id = 3, create = DATE_2, change = DATE_1, color = 2, type = NoteType.TEXT,
                rankId = 2, rankPs = 2
        )

        val unsortedList = listOf(noteFirst, noteSecond, noteThird, noteFourth)

        val changeList = listOf(noteSecond, noteFirst, noteFourth, noteThird)
        val createList = listOf(noteThird, noteFourth, noteFirst, noteSecond)
        val rankList = listOf(noteThird, noteSecond, noteFourth, noteFirst)
        val colorList = listOf(noteFirst, noteFourth, noteSecond, noteThird)
    }

}