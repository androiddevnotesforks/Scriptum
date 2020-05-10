package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.AlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.dao.IAlarmDao
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [AlarmRepo]
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepoTest : ParentIntegrationTest() {

    // TODO nullable tests

    private val badAlarmRepo: IAlarmRepo = AlarmRepo(RoomProvider(context = null))
    private val alarmRepo: IAlarmRepo = AlarmRepo(RoomProvider(context))

    private val noteConverter = NoteConverter()


    @Test fun insert() = inRoomTest {
        TODO("nullable")

        noteDao.insert(firstNote)

        val alarmEntity = firstAlarm.copy()
        val noteItem = noteConverter.toItem(firstNote)

        alarmRepo.insertOrUpdate(noteItem, alarmEntity.date)

        /**
         * For insert need default [NoteItem.haveAlarm] == false.
         * It cause [IAlarmDao.insert] return not control [AlarmEntity.id].
         */
        alarmEntity.id = noteItem.alarmId

        assertEquals(noteItem.alarmDate, alarmEntity.date)
        assertTrue(noteItem.haveAlarm())

        assertEquals(arrayListOf(getNotificationItem(firstNote, alarmEntity)), alarmRepo.getList())
    }

    @Test fun update() = inRoomTest {
        TODO("nullable")

        noteDao.insert(firstNote)

        val alarmEntity = firstAlarm.copy().apply {
            id = alarmDao.insert(alarmEntity = this)
            date = DATE_2
        }
        val noteItem = noteConverter.toItem(firstNote, alarmEntity = alarmEntity)

        alarmRepo.insertOrUpdate(noteItem, alarmEntity.date)

        assertEquals(noteItem.alarmDate, alarmEntity.date)
        assertEquals(noteItem.alarmId, alarmEntity.id)
        assertTrue(noteItem.haveAlarm())

        assertEquals(arrayListOf(getNotificationItem(firstNote, alarmEntity)), alarmRepo.getList())
    }

    @Test fun delete() = inRoomTest {
        TODO("nullable")

        noteDao.insert(firstNote)
        alarmDao.insert(firstAlarm)

        alarmRepo.delete(firstAlarm.noteId)

        assertTrue(alarmRepo.getList().isEmpty())
    }

    @Test fun getItem() = inRoomTest {
        TODO("nullable")

        assertNull(badAlarmRepo.getItem(Random.nextLong()))
        assertNull(alarmRepo.getItem(Random.nextLong()))

        noteDao.insert(firstNote)
        val firstAlarm = firstAlarm.also { it.id = alarmDao.insert(it) }

        noteDao.insert(secondNote)
        val secondAlarm = secondAlarm.also { it.id = alarmDao.insert(it) }

        assertEquals(getNotificationItem(firstNote, firstAlarm), alarmRepo.getItem(firstNote.id))
        assertEquals(getNotificationItem(secondNote, secondAlarm), alarmRepo.getItem(secondNote.id))
    }

    @Test fun getList() = inRoomTest {
        TODO("nullable")

        assertTrue(badAlarmRepo.getList().isEmpty())
        assertTrue(alarmRepo.getList().isEmpty())

        noteDao.insert(firstNote)
        val firstAlarm = firstAlarm.also { it.id = alarmDao.insert(it) }

        noteDao.insert(secondNote)
        val secondAlarm = secondAlarm.also { it.id = alarmDao.insert(it) }

        val list = arrayListOf(
                getNotificationItem(firstNote, firstAlarm),
                getNotificationItem(secondNote, secondAlarm)
        )

        assertEquals(list, alarmRepo.getList())
    }


    private fun getNotificationItem(noteEntity: NoteEntity,
                                    alarmEntity: AlarmEntity) : NotificationItem {
        return NotificationItem(with(noteEntity) {
            NotificationItem.Note(id, name, color, type)
        }, with(alarmEntity) {
            NotificationItem.Alarm(id, date)
        })
    }


    private val firstNote = NoteEntity(
            id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
            color = 1, type = NoteType.TEXT
    )

    private val secondNote = NoteEntity(
            id = 2, create = DATE_2, change = DATE_2, text = "654", name = "321",
            color = 2, type = NoteType.TEXT
    )

    private val firstAlarm = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
    private val secondAlarm = AlarmEntity(id = 2, noteId = 2, date = DATE_2)

}