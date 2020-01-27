package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.dao.IAlarmDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
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
        insertAlarmRelation(noteFirst, alarmFirst)

        val newAlarm = alarmSecond.copy(noteId = alarmFirst.noteId)
        assertEquals(newAlarm.id, alarmDao.insert(newAlarm))
    }

    @Test fun delete() = inRoomTest {
        insertAlarmRelation(noteFirst, alarmFirst)

        alarmDao.delete(alarmFirst.noteId)
        assertNull(alarmDao.get(alarmFirst.noteId))
    }

    @Test fun update() = inRoomTest {
        insertAlarmRelation(noteFirst, alarmFirst)

        alarmFirst.copy(date = DATE_2).let {
            alarmDao.update(it)
            assertEquals(it, alarmDao.get(alarmFirst.noteId))
        }
    }

    @Test fun getOnWrongId() = inRoomTest { assertNull(alarmDao.get(Random.nextLong())) }

    @Test fun getOnCorrectId() = inRoomTest {
        insertAlarmRelation(noteFirst, alarmFirst)

        assertEquals(alarmFirst, alarmDao.get(alarmFirst.noteId))
    }

    @Test fun getItem() = inRoomTest {
        assertNull(alarmDao.getItem(Random.nextLong()))

        insertAlarmRelation(noteFirst, alarmFirst)
        insertAlarmRelation(noteSecond, alarmSecond)

        assertEquals(notificationFirst, alarmDao.getItem(noteFirst.id))
        assertEquals(notificationSecond, alarmDao.getItem(noteSecond.id))
    }

    @Test fun getList() = inRoomTest {
        assertTrue(alarmDao.getList().isEmpty())

        insertAlarmRelation(noteFirst, alarmFirst)
        insertAlarmRelation(noteSecond, alarmSecond)

        assertEquals(notificationList, alarmDao.getList())
    }

    @Test fun getCount() = inRoomTest {
        var size = 0

        assertEquals(alarmDao.getCount(), size)

        insertAlarmRelation(noteFirst, alarmFirst)
        assertEquals(alarmDao.getCount(), ++size)

        insertAlarmRelation(noteSecond, alarmSecond)
        assertEquals(alarmDao.getCount(), ++size)
    }


    private companion object {
        val noteFirst = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
                color = 1, type = NoteType.TEXT
        )

        val noteSecond = NoteEntity(
                id = 2, create = DATE_2, change = DATE_2, text = "654", name = "321",
                color = 2, type = NoteType.TEXT
        )

        val alarmFirst = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
        val alarmSecond = AlarmEntity(id = 2, noteId = 2, date = DATE_2)

        val notificationFirst = NotificationItem(
                with(noteFirst) { NotificationItem.Note(id, name, color, type) },
                with(alarmFirst) { NotificationItem.Alarm(id, date) }
        )

        val notificationSecond = NotificationItem(
                with(noteSecond) { NotificationItem.Note(id, name, color, type) },
                with(alarmSecond) { NotificationItem.Alarm(id, date) }
        )

        val notificationList = arrayListOf(notificationFirst, notificationSecond)
    }

}