package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.room.BindRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [BindRepo]
 */
@RunWith(AndroidJUnit4::class)
class BindRepoTest : ParentIntegrationTest() {

    private val bindRepo: IBindRepo = BindRepo(context)

    @Test fun unbindNote() = inRoomTest {
        val noteFirst = noteFirst.copy()

        assertFalse(bindRepo.unbindNote(noteFirst.id))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteFirst))
        assertTrue(bindRepo.unbindNote(noteFirst.id))

        assertEquals(noteFirst.apply { isStatus = false }, noteDao.get(noteFirst.id))
    }

    @Test fun getNotificationCount() = inRoomTest {
        var size = 0
        assertEquals(size, bindRepo.getNotificationCount())

        insertAlarmRelation(noteFirst, alarmFirst)
        assertEquals(++size, bindRepo.getNotificationCount())

        insertAlarmRelation(noteSecond, alarmSecond)
        assertEquals(++size, bindRepo.getNotificationCount())
    }

    private suspend fun RoomDb.insertAlarmRelation(noteEntity: NoteEntity,
                                                   alarmEntity: AlarmEntity) {
        noteDao.insert(noteEntity)
        alarmDao.insert(alarmEntity)
    }


    private companion object {
        val noteFirst = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, type = NoteType.TEXT, isStatus = true
        )

        val noteSecond = NoteEntity(
                id = 2, create = DATE_1, change = DATE_1, type = NoteType.ROLL, isStatus = true
        )

        val alarmFirst = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
        val alarmSecond = AlarmEntity(id = 2, noteId = 2, date = DATE_2)
    }

}