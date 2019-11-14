package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
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
        TODO("REFACTOR")
//        textNote.let {
//            iNoteDao.insert(it)
//
//            assertEquals(it.apply { isStatus = false }, iRepo.unbindNote(it.id))
//            assertEquals(it, iNoteDao[it.id])
//        }
    }

    @Test fun getNotificationCount() = inRoom {
        val insertAlarmRelation = { noteEntity: NoteEntity, alarmEntity: AlarmEntity ->
            iNoteDao.insert(noteEntity)
            iAlarmDao.insert(alarmEntity)
        }

        assertEquals(iAlarmDao.getCount(), 0)

        insertAlarmRelation(textNote, alarmFirst)
        assertEquals(iAlarmDao.getCount(), 1)

        insertAlarmRelation(rollNote, alarmSecond)
        assertEquals(iAlarmDao.getCount(), 2)
    }

    private companion object {
        val textNote = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, type = NoteType.TEXT, isStatus = true
        )

        val rollNote = NoteEntity(
                id = 2, create = DATE_1, change = DATE_1, type = NoteType.ROLL, isStatus = true
        )

        val alarmFirst = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
        val alarmSecond = AlarmEntity(id = 2, noteId = 2, date = DATE_2)
    }

}