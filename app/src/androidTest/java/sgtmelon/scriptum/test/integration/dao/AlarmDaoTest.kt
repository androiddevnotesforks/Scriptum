package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.dao.AlarmDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Интеграционный тест для [AlarmDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentIntegrationTest() {

    private fun RoomDb.insertAlarmRelation(noteEntity: NoteEntity, alarmEntity: AlarmEntity) {
        getNoteDao().insert(noteEntity)
        getAlarmDao().insert(alarmEntity)
    }

    @Test fun getOnWrongId() = inTheRoom { assertNull(getAlarmDao()[Random.nextLong()]) }

    @Test fun getOnCorrectId() = inTheRoom {
        insertAlarmRelation(noteFirst, alarmFirst)

        assertEquals(alarmFirst, getAlarmDao()[alarmFirst.id])
    }

    @Test fun getList() = inTheRoom {
        assertTrue(getAlarmDao().get().isEmpty())

        insertAlarmRelation(noteFirst, alarmFirst)
        insertAlarmRelation(noteSecond, alarmSecond)

        assertEquals(notificationList, getAlarmDao().get())
    }

    @Test fun delete() = inTheRoom {
        insertAlarmRelation(noteFirst, alarmFirst)

        getAlarmDao().delete(alarmFirst.id)

        assertNull(getAlarmDao()[alarmFirst.id])
    }

    @Test fun update() = inTheRoom {
        insertAlarmRelation(noteFirst, alarmFirst)

        alarmFirst.copy().let {
            getAlarmDao().update(it.apply { date = DATE_2 })
            assertEquals(it, getAlarmDao()[alarmFirst.id])
        }
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