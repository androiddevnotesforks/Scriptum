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

    private fun RoomDb.insertAlarmRelation(noteEntity: NoteEntity, alarmEntity: AlarmEntity) {
        iNoteDao.insert(noteEntity)
        iAlarmDao.insert(alarmEntity)
    }

    @Test fun insertWithUnique() = inRoom {
        insertAlarmRelation(noteFirst, alarmFirst)
        iAlarmDao.insert(alarmFirst)

        assertTrue(iAlarmDao.get().size == 1)
    }

    @Test fun delete() = inRoom {
        insertAlarmRelation(noteFirst, alarmFirst)

        iAlarmDao.delete(alarmFirst.noteId)
        assertNull(iAlarmDao[alarmFirst.noteId])
    }

    @Test fun update() = inRoom {
        insertAlarmRelation(noteFirst, alarmFirst)

        alarmFirst.copy(date = DATE_2).let {
            iAlarmDao.update(it)
            assertEquals(it, iAlarmDao[alarmFirst.noteId])
        }
    }

    @Test fun getOnWrongId() = inRoom { assertNull(iAlarmDao[Random.nextLong()]) }

    @Test fun getOnCorrectId() = inRoom {
        insertAlarmRelation(noteFirst, alarmFirst)

        assertEquals(alarmFirst, iAlarmDao[alarmFirst.noteId])
    }

    @Test fun getList() = inRoom {
        assertTrue(iAlarmDao.get().isEmpty())

        insertAlarmRelation(noteFirst, alarmFirst)
        insertAlarmRelation(noteSecond, alarmSecond)

        assertEquals(notificationList, iAlarmDao.get())
    }

    @Test fun getCount() = inRoom {
        assertEquals(iAlarmDao.getCount(), 0)

        insertAlarmRelation(noteFirst, alarmFirst)
        assertEquals(iAlarmDao.getCount(), 1)

        insertAlarmRelation(noteSecond, alarmSecond)
        assertEquals(iAlarmDao.getCount(), 2)
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

        val notificationList = arrayListOf(NotificationItem(
                with(noteFirst) { NotificationItem.Note(id, name, color, type) },
                with(alarmFirst) { NotificationItem.Alarm(id, date) }
        ), NotificationItem(
                with(noteSecond) { NotificationItem.Note(id, name, color, type) },
                with(alarmSecond) { NotificationItem.Alarm(id, date) }
        ))
    }

}