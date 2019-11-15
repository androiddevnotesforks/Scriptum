package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [BindRepo]
 */
@RunWith(AndroidJUnit4::class)
class BindRepoTest : ParentIntegrationTest() {

    private val iRepo: IBindRepo = BindRepo(context)

    @Test fun unbindNote() = inRoom {
        val noteFirst = noteFirst.copy()

        assertFalse(iRepo.unbindNote(noteFirst.id))
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteFirst))
        assertTrue(iRepo.unbindNote(noteFirst.id))

        assertEquals(noteFirst.apply { isStatus = false }, iNoteDao[noteFirst.id])
    }

    @Test fun getNotificationCount() = inRoom {
        val insertAlarmRelation = { noteEntity: NoteEntity, alarmEntity: AlarmEntity ->
            iNoteDao.insert(noteEntity)
            iAlarmDao.insert(alarmEntity)
        }

        var size = 0
        assertEquals(iAlarmDao.getCount(), size)

        insertAlarmRelation(noteFirst, alarmFirst)
        assertEquals(iAlarmDao.getCount(), ++size)

        insertAlarmRelation(noteSecond, alarmSecond)
        assertEquals(iAlarmDao.getCount(), ++size)
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