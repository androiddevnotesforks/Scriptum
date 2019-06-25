package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.dao.AlarmDao
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Интеграционный тест для [AlarmDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentIntegrationTest() {

    @Test fun allFunctions() = openRoom().apply {
        assertNull(getAlarmDao()[alarmEntity.id])

        getNoteDao().insert(noteEntity)
        getAlarmDao().insert(alarmEntity)

        assertEquals(alarmEntity, getAlarmDao()[noteEntity.id])

        getAlarmDao().update(alarmEntity.apply { date = DATE_2 })

        getAlarmDao().get().let {
            assertTrue(it.size == 1)
            assertEquals(notificationItem, it[0])
        }

        getAlarmDao().delete(alarmEntity.id)
        assertNull(getAlarmDao()[alarmEntity.id])
    }.close()

    companion object {
        val noteEntity = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, text = "123", name = "456",
                color = 1, type = NoteType.TEXT
        )

        val alarmEntity = AlarmEntity(id = 1, noteId = 1, date = DATE_1)

        val notificationItem = NotificationItem(
                with(noteEntity) { NotificationItem.Note(id, name, color, type) },
                NotificationItem.Alarm(alarmEntity.id, DATE_2)
        )
    }

}