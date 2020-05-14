package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.repository.room.BindRepo
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [BindRepo]
 */
@RunWith(AndroidJUnit4::class)
class BindRepoTest : ParentIntegrationTest() {

    // TODO make test only related with dao
    // TODO nullable tests

//    private val badBindRepo: IBindRepo = BindRepo(RoomProvider(context = null))
//    private val bindRepo: IBindRepo = BindRepo(RoomProvider(context))
//
//    @Test fun unbindNote() = inRoomTest {
//        TODO("nullable")
//
//        val item = firstNote.copy()
//
//        assertFalse(badBindRepo.unbindNote(item.id))
//
//        assertFalse(bindRepo.unbindNote(item.id))
//        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(item))
//        assertTrue(bindRepo.unbindNote(item.id))
//
//        assertEquals(item.apply { isStatus = false }, noteDao.get(item.id))
//    }
//
//    @Test fun getNotificationCount() = inRoomTest {
//        TODO("nullable")
//
//        assertEquals(0, badBindRepo.getNotificationCount())
//
//        var size = 0
//        assertEquals(size, bindRepo.getNotificationCount())
//
//        insertAlarmRelation(firstNote, firstAlarm)
//        assertEquals(++size, bindRepo.getNotificationCount())
//
//        insertAlarmRelation(secondNote, secondAlarm)
//        assertEquals(++size, bindRepo.getNotificationCount())
//    }
//
//    private suspend fun RoomDb.insertAlarmRelation(noteEntity: NoteEntity,
//                                                   alarmEntity: AlarmEntity) {
//        noteDao.insert(noteEntity)
//        alarmDao.insert(alarmEntity)
//    }
//
//
//    private companion object {
//        val firstNote = NoteEntity(
//                id = 1, create = DATE_1, change = DATE_1, type = NoteType.TEXT, isStatus = true
//        )
//
//        val secondNote = NoteEntity(
//                id = 2, create = DATE_1, change = DATE_1, type = NoteType.ROLL, isStatus = true
//        )
//
//        val firstAlarm = AlarmEntity(id = 1, noteId = 1, date = DATE_1)
//        val secondAlarm = AlarmEntity(id = 2, noteId = 2, date = DATE_2)
//    }

}