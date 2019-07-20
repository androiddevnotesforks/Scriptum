package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [AlarmRepo]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class AlarmRepoTest : ParentIntegrationTest() {

    private val iAlarmRepo = AlarmRepo.getInstance(context)

    @Test fun getList() = openRoom().apply {
        getNoteDao().insert(noteEntity)
        getAlarmDao().insert(alarmEntity)

        assertEquals(arrayListOf(notificationItem), iAlarmRepo.getList())
    }.close()

    @Test fun delete() = openRoom().apply {
        getNoteDao().insert(noteEntity)
        getAlarmDao().insert(alarmEntity)

        iAlarmRepo.delete(alarmEntity.id)

        assertTrue(iAlarmRepo.getList().isEmpty())
    }.close()

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