package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IAlarmDao
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [IAlarmDao]
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentIntegrationTest() {

    private suspend fun RoomDb.insertAlarmRelation(noteEntity: NoteEntity,
                                                   alarmEntity: AlarmEntity) {
        noteDao.insert(noteEntity)
        alarmDao.insert(alarmEntity)
    }


    @Test fun insertWithUnique() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val newAlarm = secondAlarm.copy(noteId = firstAlarm.noteId)
        assertEquals(newAlarm.id, alarmDao.insert(newAlarm))
    }

    @Test fun delete() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        alarmDao.delete(firstAlarm.noteId)
        assertNull(alarmDao.get(firstAlarm.noteId))
    }

    @Test fun update() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        firstAlarm.copy(date = DATE_2).let {
            alarmDao.update(it)
            assertEquals(it, alarmDao.get(firstAlarm.noteId))
        }
    }

    @Test fun getOnWrongId() = inRoomTest { assertNull(alarmDao.get(Random.nextLong())) }

    @Test fun getOnCorrectId() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        assertEquals(firstAlarm, alarmDao.get(firstAlarm.noteId))
    }

    @Test fun getListById() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)
        insertAlarmRelation(secondNote, secondAlarm)

        val alarmList = listOf(firstAlarm, secondAlarm)
        val noteIdList = listOf(firstNote.id, secondNote.id)

        assertEquals(alarmList, alarmDao.get(noteIdList))
    }

    @Test fun getItem() = inRoomTest {
        assertNull(alarmDao.getItem(Random.nextLong()))

        insertAlarmRelation(firstNote, firstAlarm)
        insertAlarmRelation(secondNote, secondAlarm)

        assertEquals(firstNotification, alarmDao.getItem(firstNote.id))
        assertEquals(secondNotification, alarmDao.getItem(secondNote.id))
    }

    @Test fun getList() = inRoomTest {
        assertTrue(alarmDao.getList().isEmpty())

        insertAlarmRelation(firstNote, firstAlarm)
        insertAlarmRelation(secondNote, secondAlarm)

        assertEquals(notificationList, alarmDao.getList())
    }

    @Test fun getCount() = inRoomTest {
        var size = 0

        assertEquals(alarmDao.getCount(), size)

        insertAlarmRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.getCount(), ++size)

        insertAlarmRelation(secondNote, secondAlarm)
        assertEquals(alarmDao.getCount(), ++size)
    }


    private companion object {
        val firstNote = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
                color = 1, type = NoteType.TEXT
        )

        val secondNote = NoteEntity(
                id = 2, create = DATE_2, change = DATE_2, text = "654", name = "321",
                color = 2, type = NoteType.TEXT
        )

        val firstAlarm = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
        val secondAlarm = AlarmEntity(id = 2, noteId = 2, date = DATE_2)

        val firstNotification = NotificationItem(
                with(firstNote) { NotificationItem.Note(id, name, color, type) },
                with(firstAlarm) { NotificationItem.Alarm(id, date) }
        )

        val secondNotification = NotificationItem(
                with(secondNote) { NotificationItem.Note(id, name, color, type) },
                with(secondAlarm) { NotificationItem.Alarm(id, date) }
        )

        val notificationList = arrayListOf(firstNotification, secondNotification)
    }

}