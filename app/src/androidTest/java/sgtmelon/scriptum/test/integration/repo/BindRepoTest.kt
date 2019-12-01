package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [BindRepo]
 */
@RunWith(AndroidJUnit4::class)
class BindRepoTest : ParentIntegrationTest() {

    private val iBindRepo: IBindRepo = BindRepo(context)

    @Test fun unbindNote() = inRoomTest {
        val noteFirst = noteFirst.copy()

        assertFalse(iBindRepo.unbindNote(noteFirst.id))
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteFirst))
        assertTrue(iBindRepo.unbindNote(noteFirst.id))

        assertEquals(noteFirst.apply { isStatus = false }, iNoteDao[noteFirst.id])
    }

    @Test fun getNotificationCount() = inRoomTest {
        var size = 0
        assertEquals(size, iBindRepo.getNotificationCount())

        insertAlarmRelation(noteFirst, alarmFirst)
        assertEquals(++size, iBindRepo.getNotificationCount())

        insertAlarmRelation(noteSecond, alarmSecond)
        assertEquals(++size, iBindRepo.getNotificationCount())
    }

    private suspend fun RoomDb.insertAlarmRelation(noteEntity: NoteEntity,
                                                   alarmEntity: AlarmEntity) {
        iNoteDao.insert(noteEntity)
        iAlarmDao.insert(alarmEntity)
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