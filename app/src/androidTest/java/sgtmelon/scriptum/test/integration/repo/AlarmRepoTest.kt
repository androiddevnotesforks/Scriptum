package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentTest

/**
 * Интеграционный тест для [AlarmRepo]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepoTest : ParentTest() {

    private fun openRoom() = RoomDb.getInstance(context)

    private val iAlarmRepo = AlarmRepo.getInstance(context)

    @Test fun getList() = openRoom().apply {
        clearAllTables()

        getNoteDao().insert(noteEntity)
        getAlarmDao().insert(alarmEntity)

        assertEquals(arrayListOf(notificationItem), iAlarmRepo.getList())
    }.close()

    @Test fun delete() = openRoom().apply {
        clearAllTables()

        getNoteDao().insert(noteEntity)
        getAlarmDao().insert(alarmEntity)

        iAlarmRepo.delete(alarmEntity.id)

        assertTrue(iAlarmRepo.getList().isEmpty())
    }.close()

    private companion object {
        val noteEntity = NoteEntity(id = 1, name = "Notification", color = 0, type = NoteType.TEXT)
        val alarmEntity = AlarmEntity(id = 1, noteId = 1, date = "1921-01-12 01:02:03")

        val notificationItem = NotificationItem(
                NotificationItem.Note(noteEntity.id, noteEntity.name, noteEntity.color, noteEntity.type),
                NotificationItem.Alarm(alarmEntity.id, alarmEntity.date)
        )
    }

}