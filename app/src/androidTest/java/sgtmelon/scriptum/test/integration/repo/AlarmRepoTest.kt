package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [AlarmRepo]
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepoTest : ParentIntegrationTest() {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    @Test fun insert() = inRoom {
        val alarmEntity = alarmEntity.copy()

        iNoteDao.insert(noteEntity)
        iAlarmRepo.insertOrUpdate(alarmEntity)

        assertEquals(arrayListOf(getNotificationItem(alarmEntity)), iAlarmRepo.getList())
    }

    @Test fun update() = inRoom {
        val alarmEntity = alarmEntity

        iNoteDao.insert(noteEntity)
        alarmEntity.let {
            it.id = iAlarmDao.insert(it)
            it.date = ""

            iAlarmRepo.insertOrUpdate(it)
            assertEquals(arrayListOf(getNotificationItem(it)), iAlarmRepo.getList())
        }
    }

    @Test fun delete() = inRoom {
        iNoteDao.insert(noteEntity)
        iAlarmDao.insert(alarmEntity)

        iAlarmRepo.delete(alarmEntity.noteId)

        assertTrue(iAlarmRepo.getList().isEmpty())
    }

    @Test fun getList() = inRoom {
        val alarmEntity = alarmEntity

        iNoteDao.insert(noteEntity)
        alarmEntity.let { it.id = iAlarmDao.insert(it) }

        assertEquals(arrayListOf(getNotificationItem(alarmEntity)), iAlarmRepo.getList())
    }


    private fun getNotificationItem(alarmEntity: AlarmEntity) : NotificationItem {
        return NotificationItem(notificationNote, with(alarmEntity) {
            NotificationItem.Alarm(id, date)
        })
    }

    private companion object {
        val noteEntity = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
                color = 5, type = NoteType.TEXT
        )

        val alarmEntity = AlarmEntity(noteId = 1, date = DATE_1)

        val notificationNote = with(noteEntity) { NotificationItem.Note(id, name, color, type) }
    }

}