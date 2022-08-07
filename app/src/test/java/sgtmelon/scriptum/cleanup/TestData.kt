package sgtmelon.scriptum.cleanup

import kotlin.random.Random
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem.Note
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.test.common.nextString

/**
 * Class which provide data for tests.
 */
object TestData {

    // TODO replace item manipulations with mockkObject calls

    object Rank {
        val firstRank get() = RankItem(
                id = 1, noteId = mutableListOf(1, 2), position = 0, name = "1", isVisible = true
        )
        val secondRank get() = RankItem(
                id = 2, noteId = mutableListOf(2, 3), position = 1, name = "2", isVisible = false
        )
        val thirdRank get() = RankItem(
                id = 3, noteId = mutableListOf(1, 5), position = 2, name = "3", isVisible = true
        )
        val fourthRank get() = RankItem(
                id = 4, noteId = mutableListOf(4, 6), position = 3, name = "4", isVisible = false
        )

        val itemList get() = mutableListOf(firstRank, secondRank, thirdRank, fourthRank)

        val firstCorrectList get() = mutableListOf(secondRank, thirdRank, firstRank, fourthRank)
        val secondCorrectList get() = mutableListOf(fourthRank, secondRank, firstRank, thirdRank)

        val firstCorrectPosition get() = mutableListOf(2L, 3, 1, 5)
        val secondCorrectPosition get() = mutableListOf(4L, 6, 1, 2, 5)
    }

    object Note {
        const val DATE_0 = "1234-01-02 03:04:05"
        const val DATE_1 = "1345-02-03 04:05:06"
        const val DATE_2 = "1456-03-04 05:06:07"
        const val DATE_3 = "1567-04-05 06:07:08"

        val firstNote get() = NoteItem.Text(
                id = 0, create = DATE_1, change = DATE_2, color = Color.RED,
                rankId = -1, rankPs = -1, alarmId = 1, alarmDate = DATE_3
        )

        val secondNote get() = NoteItem.Roll(
                id = 1, create = DATE_0, change = DATE_3, color = Color.INDIGO,
                rankId = 1, rankPs = 1, isStatus = true
        )

        val thirdNote get() = NoteItem.Text(
            id = 2, create = DATE_3, change = DATE_0, color = Color.TEAL,
            rankId = 1, rankPs = 1, alarmId = 2, alarmDate = DATE_2
        )

        val fourthNote
            get() = NoteItem.Roll(
                id = 3, create = DATE_2, change = DATE_1, color = Color.INDIGO,
                rankId = 2, rankPs = 2, isStatus = true
            )

        val itemList get() = mutableListOf(firstNote, secondNote, thirdNote, fourthNote)

        val changeList get() = listOf(secondNote, firstNote, fourthNote, thirdNote)
        val createList get() = listOf(thirdNote, fourthNote, firstNote, secondNote)
        val rankList get() = listOf(secondNote, thirdNote, fourthNote, firstNote)
        val colorList get() = listOf(firstNote, secondNote, fourthNote, thirdNote)

        val dateList = listOf(DATE_0, DATE_1, DATE_2, DATE_3)

        val rollList
            get() = mutableListOf(
                RollItem(id = 0, position = 0, isCheck = Random.nextBoolean(), text = nextString()),
                RollItem(id = 1, position = 1, isCheck = Random.nextBoolean(), text = nextString()),
                RollItem(id = 2, position = 2, isCheck = Random.nextBoolean(), text = nextString()),
                RollItem(id = 3, position = 3, isCheck = Random.nextBoolean(), text = nextString()),
                RollItem(id = 4, position = 4, isCheck = Random.nextBoolean(), text = nextString()),
                RollItem(id = 5, position = 5, isCheck = Random.nextBoolean(), text = nextString())
            )

        val rankIdVisibleList get() = List(size = 5) { Random.nextLong() }
    }

    object Notification {
        val firstNotification = NotificationItem(
                Note(id = 0, name = "testName1", color = Color.GREEN, type = NoteType.TEXT),
                Alarm(id = 0, date = "123")
        )

        val secondNotification = NotificationItem(
                Note(id = 1, name = "testName2", color = Color.BLUE, type = NoteType.ROLL),
                Alarm(id = 1, date = "456")
        )

        val thirdNotification = NotificationItem(
                Note(id = 2, name = "testName3", color = Color.BROWN, type = NoteType.TEXT),
                Alarm(id = 2, date = "789")
        )

        val itemList get() = mutableListOf(firstNotification, secondNotification, thirdNotification)
    }

    object Melody {
        private val alarmList = listOf(
                MelodyItem(title = "alarm_title_0", uri = "alarm_uri_0", id = "alarm_id_0"),
                MelodyItem(title = "alarm_title_1", uri = "alarm_uri_1", id = "alarm_id_1")
        )

        private val ringtoneList = listOf(
                MelodyItem(title = "ringtone_title_0", uri = "ringtone_uri_0", id = "ringtone_id_0"),
                MelodyItem(title = "ringtone_title_1", uri = "ringtone_uri_1", id = "ringtone_id_1")
        )

        val melodyList = ArrayList<MelodyItem>().apply {
            addAll(alarmList)
            addAll(ringtoneList)
        }.sortedBy { it.title }
    }

    object Backup {
        val fileList = listOf(
                FileItem(nextString(), nextString()),
                FileItem(nextString(), nextString()),
                FileItem(nextString(), nextString()),
                FileItem(nextString(), nextString())
        )
    }

}