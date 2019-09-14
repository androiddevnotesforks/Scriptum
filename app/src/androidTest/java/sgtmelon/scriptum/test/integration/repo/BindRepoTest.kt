package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.bind.IBindRepo
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [BindRepo]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class BindRepoTest : ParentIntegrationTest() {

    private val iRoomBindRepo: IBindRepo = BindRepo(context)

    @Test fun getRollListOnTextNote() = inRoom {
        textNote.let {
            iNoteDao.insert(it)

            assertTrue(iRoomBindRepo.getRollList(it.id).isEmpty())
        }
    }

    @Test fun getRollListOnRollNote() = inRoom {
        rollNote.let {
            iNoteDao.insert(it)

            assertTrue(iRoomBindRepo.getRollList(it.id).isEmpty())

            rollList.forEach { item -> iRollDao.insert(item) }

            assertEquals(rollList, iRoomBindRepo.getRollList(it.id))
        }
    }

    @Test fun unbindNote() = inRoom {
        textNote.let {
            iNoteDao.insert(it)

            assertEquals(it.apply { isStatus = false }, iRoomBindRepo.unbindNote(it.id))
            assertEquals(it, iNoteDao[it.id])
        }
    }

    private companion object {
        val textNote = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, type = NoteType.TEXT, isStatus = true
        )
        val rollNote = NoteEntity(
                id = 1, create = DATE_1, change = DATE_1, type = NoteType.ROLL, isStatus = true
        )

        val rollList: List<RollEntity> = arrayListOf(
                RollEntity(id = 0, noteId = 1, position = 0, isCheck = false, text = "0"),
                RollEntity(id = 1, noteId = 1, position = 1, isCheck = false, text = "1"),
                RollEntity(id = 2, noteId = 1, position = 2, isCheck = true, text = "02"),
                RollEntity(id = 3, noteId = 1, position = 3, isCheck = true, text = "03")
        )
    }

}