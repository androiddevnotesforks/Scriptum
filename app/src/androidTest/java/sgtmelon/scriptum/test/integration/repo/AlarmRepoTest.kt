package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.room.alarm.IRoomAlarmRepo
import sgtmelon.scriptum.repository.room.alarm.RoomAlarmRepo
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [RoomAlarmRepo]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepoTest : ParentIntegrationTest() {

    private val iRoomAlarmRepo: IRoomAlarmRepo = RoomAlarmRepo(context)

    @Test fun insert() = inRoom {
        iNoteDao.insert(noteEntity)

        iRoomAlarmRepo.insertOrUpdate(alarmEntity)

        assertEquals(arrayListOf(notificationItem), iRoomAlarmRepo.getList())
    }

    @Test fun update() = inRoom {
        iNoteDao.insert(noteEntity)
        iAlarmDao.insert(alarmEntity)

        alarmEntity.copy(date = "").let {
            iRoomAlarmRepo.insertOrUpdate(it)
            assertNotEquals(arrayListOf(notificationItem), iRoomAlarmRepo.getList())
        }
    }

    @Test fun delete() = inRoom {
        iNoteDao.insert(noteEntity)
        iAlarmDao.insert(alarmEntity)

        iRoomAlarmRepo.delete(alarmEntity.noteId)

        assertTrue(iRoomAlarmRepo.getList().isEmpty())
    }

    @Test fun getList() = inRoom {
        iNoteDao.insert(noteEntity)
        iAlarmDao.insert(alarmEntity)

        assertEquals(arrayListOf(notificationItem), iRoomAlarmRepo.getList())
    }

    private companion object {
        val noteEntity = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
                color = 5, type = NoteType.TEXT
        )

        val alarmEntity = AlarmEntity(id = 1, noteId = 1, date = DATE_1)

        val notificationItem = NotificationItem(
                with(noteEntity) { NotificationItem.Note(id, name, color, type) },
                with(alarmEntity) { NotificationItem.Alarm(id, date) }
        )
    }

}