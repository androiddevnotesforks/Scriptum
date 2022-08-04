package sgtmelon.scriptum.integrational.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2

/**
 * Integration test for [AlarmDao].
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentRoomTest() {

    //region Variables

    private val firstNote = NoteEntity(
        id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
        color = Color.PURPLE, type = NoteType.TEXT
    )

    private val secondNote = NoteEntity(
        id = 2, create = DATE_2, change = DATE_2, text = "654", name = "321",
        color = Color.INDIGO, type = NoteType.TEXT
    )

    private val firstAlarm = AlarmEntity(id = 1, noteId = firstNote.id, date = DATE_1)
    private val secondAlarm = AlarmEntity(id = 2, noteId = secondNote.id, date = DATE_2)

    private val firstNotification = NotificationItem(
        with(firstNote) { NotificationItem.Note(id, name, color, type) },
        with(firstAlarm) { NotificationItem.Alarm(id, date) }
    )

    private val secondNotification = NotificationItem(
        with(secondNote) { NotificationItem.Note(id, name, color, type) },
        with(secondAlarm) { NotificationItem.Alarm(id, date) }
    )

    private val notificationList = arrayListOf(firstNotification, secondNotification)

    //endregion

    private suspend fun RoomDb.insertAlarmRelation(note: NoteEntity, alarm: AlarmEntity) {
        noteDao.insert(note)
        alarmDao.insert(alarm)
    }

    // Dao tests

    @Test fun insert() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.get(firstNote.id), firstAlarm)
    }

    @Test fun insertWithConflict() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val conflictAlarm = secondAlarm.copy(id = firstAlarm.id)
        alarmDao.insert(conflictAlarm)

        assertEquals(alarmDao.get(firstNote.id), conflictAlarm)
    }

    @Test fun insertWithNoteIdUnique() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val uniqueAlarm = secondAlarm.copy(noteId = firstNote.id)
        assertEquals(alarmDao.insert(uniqueAlarm), uniqueAlarm.id)

        assertEquals(alarmDao.get(firstNote.id), uniqueAlarm)
    }

    @Test fun getCountByIdListCrowd() = inRoomTest { alarmDao.getCount(crowdLongList) }
}