package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.room.converter.NoteConverter
import sgtmelon.scriptum.room.dao.IAlarmDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [AlarmRepo]
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepoTest : ParentIntegrationTest() {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    private val noteConverter = NoteConverter()


    @Test fun insert() = inRoomTest {
        iNoteDao.insert(noteFirst)

        val alarmFirst = alarmFirst.copy()
        val noteItem = noteConverter.toItem(noteFirst)

        iAlarmRepo.insertOrUpdate(noteItem, alarmFirst.date)

        /**
         * For insert need default [NoteItem.haveAlarm] == false.
         * It cause [IAlarmDao.insert] return not control [AlarmEntity.id].
         */
        alarmFirst.id = noteItem.alarmId

        assertEquals(noteItem.alarmDate, alarmFirst.date)
        assertTrue(noteItem.haveAlarm())

        assertEquals(arrayListOf(getNotificationItem(noteFirst, alarmFirst)), iAlarmRepo.getList())
    }

    @Test fun update() = inRoomTest {
        iNoteDao.insert(noteFirst)

        val alarmFirst = alarmFirst.copy().apply {
            id = iAlarmDao.insert(alarmEntity = this)
            date = DATE_2
        }
        val noteItem = noteConverter.toItem(noteFirst, alarmEntity = alarmFirst)

        iAlarmRepo.insertOrUpdate(noteItem, alarmFirst.date)

        assertEquals(noteItem.alarmDate, alarmFirst.date)
        assertEquals(noteItem.alarmId, alarmFirst.id)
        assertTrue(noteItem.haveAlarm())

        assertEquals(arrayListOf(getNotificationItem(noteFirst, alarmFirst)), iAlarmRepo.getList())
    }

    @Test fun delete() = inRoomTest {
        iNoteDao.insert(noteFirst)
        iAlarmDao.insert(alarmFirst)

        iAlarmRepo.delete(alarmFirst.noteId)

        assertTrue(iAlarmRepo.getList().isEmpty())
    }

    @Test fun getItem() = inRoomTest {
        assertNull(iAlarmRepo.getItem(Random.nextLong()))

        iNoteDao.insert(noteFirst)
        val alarmFirst = alarmFirst.also { it.id = iAlarmDao.insert(it) }

        iNoteDao.insert(noteSecond)
        val alarmSecond = alarmSecond.also { it.id = iAlarmDao.insert(it) }

        assertEquals(getNotificationItem(noteFirst, alarmFirst), iAlarmRepo.getItem(noteFirst.id))
        assertEquals(getNotificationItem(noteSecond, alarmSecond), iAlarmRepo.getItem(noteSecond.id))
    }

    @Test fun getList() = inRoomTest {
        assertTrue(iAlarmRepo.getList().isEmpty())

        iNoteDao.insert(noteFirst)
        val alarmFirst = alarmFirst.also { it.id = iAlarmDao.insert(it) }

        iNoteDao.insert(noteSecond)
        val alarmSecond = alarmSecond.also { it.id = iAlarmDao.insert(it) }

        val list = arrayListOf(
                getNotificationItem(noteFirst, alarmFirst),
                getNotificationItem(noteSecond, alarmSecond)
        )

        assertEquals(list, iAlarmRepo.getList())
    }


    private fun getNotificationItem(noteEntity: NoteEntity,
                                    alarmEntity: AlarmEntity) : NotificationItem {
        return NotificationItem(with(noteEntity) {
            NotificationItem.Note(id, name, color, type)
        }, with(alarmEntity) {
            NotificationItem.Alarm(id, date)
        })
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
    }

}