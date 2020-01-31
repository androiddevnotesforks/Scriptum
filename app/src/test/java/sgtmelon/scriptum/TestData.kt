package sgtmelon.scriptum

import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.model.item.NotificationItem.Note
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType

/**
 * Class which provide data for tests.
 */
object TestData {

    object Rank {
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

    object Note {
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

    object Notification {
        val notificationFirst = NotificationItem(
                Note(id = 0, name = "testName1", color = 5, type = NoteType.TEXT),
                Alarm(id = 0, date = "123")
        )

        val notificationSecond = NotificationItem(
                Note(id = 1, name = "testName2", color = 3, type = NoteType.ROLL),
                Alarm(id = 1, date = "456")
        )

        val notificationThird = NotificationItem(
                Note(id = 2, name = "testName3", color = 8, type = NoteType.TEXT),
                Alarm(id = 2, date = "789")
        )

        val itemList = mutableListOf(notificationFirst, notificationSecond, notificationThird)
    }

}