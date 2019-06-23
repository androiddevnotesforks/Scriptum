package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.test.ParentTest

/**
 * Интеграционный тест для [BindRepo]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class BindRepoTest : ParentTest() {

    private fun openRoom() = RoomDb.getInstance(context)

    private val iBindRepo = BindRepo.getInstance(context)

    @Test fun getRollList_onTextNote() = openRoom().apply {
        clearAllTables()

        textNote.let {
            getNoteDao().insert(it)

            assertTrue(iBindRepo.getRollList(it.id).isEmpty())
        }
    }.close()

    @Test fun getRollList_onRollNote() = openRoom().apply {
        clearAllTables()

        rollNote.let {
            getNoteDao().insert(it)

            assertTrue(iBindRepo.getRollList(it.id).isEmpty())

            rollList.forEach { item -> getRollDao().insert(item) }

            assertEquals(rollList, iBindRepo.getRollList(it.id))
        }
    }.close()

    @Test fun unbindNote() = openRoom().apply {
        clearAllTables()

        textNote.let {
            getNoteDao().insert(it)

            assertEquals(it.apply { isStatus = false }, iBindRepo.unbindNote(it.id))
            assertEquals(it, getNoteDao()[it.id])
        }
    }.close()

    private companion object {
        val textNote = NoteEntity(id = 1, type = NoteType.TEXT, isStatus = true)
        val rollNote = NoteEntity(id = 1, type = NoteType.ROLL, isStatus = true)

        val rollList: List<RollEntity> = arrayListOf(
                RollEntity(id = 0, noteId = 1, position = 0, isCheck = false, text = "0"),
                RollEntity(id = 1, noteId = 1, position = 1, isCheck = false, text = "1"),
                RollEntity(id = 2, noteId = 1, position = 2, isCheck = true, text = "02"),
                RollEntity(id = 3, noteId = 1, position = 3, isCheck = true, text = "03")
        )
    }

}