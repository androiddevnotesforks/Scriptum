package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.dao.IRollDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [IRollDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RollDaoTest : ParentIntegrationTest() {

    private fun RoomDb.insertRollRelation(noteModel: NoteModel) = with(noteModel) {
        iNoteDao.insert(noteEntity)
        rollList.forEach { iRollDao.insert(it) }
    }

    @Test fun insertWithUnique() = inRoom {
        noteFirst.let {
            insertRollRelation(it)

            it.rollList.forEach { item -> iRollDao.insert(item) }
            assertEquals(it.rollList, iRollDao[it.noteEntity.id])
        }
    }

    @Test fun update() = inRoom {
        insertRollRelation(noteSecond)

        with(noteSecond) {
            rollList[0].copy(position = 4, isCheck = true, text = "00000").let {
                iRollDao.update(it.id!!, it.position, it.text)
                iRollDao.update(it.id!!, it.isCheck)

                assertTrue(iRollDao[noteEntity.id].contains(it))
            }
        }
    }

    @Test fun updateCheck() = inRoom {
        insertRollRelation(noteSecond)

        with(noteSecond) {
            iRollDao.updateAllCheck(noteEntity.id, check = true)
            assertEquals(copy().rollList.apply {
                forEach { it.isCheck = true }
            }, iRollDao[noteEntity.id])
        }
    }

    @Test fun delete() = inRoom {
        insertRollRelation(noteFirst)

        with(noteFirst) {
            val listSave = rollList.filter { it.isCheck }

            noteEntity.id.let { id ->
                iRollDao.delete(id, listSave.map { it.id ?: -1 })
                assertEquals(listSave, iRollDao[id])
            }
        }
    }

    @Test fun deleteAll() = inRoom {
        insertRollRelation(noteFirst)

        noteFirst.noteEntity.id.let {
            iRollDao.delete(it)
            assertTrue(iRollDao[it].isEmpty())
        }
    }

    @Test fun get() = inRoom {
        noteFirst.let {
            insertRollRelation(it)
            assertEquals(it.rollList, iRollDao[it.noteEntity.id])
        }

        noteSecond.let {
            insertRollRelation(it)
            assertEquals(it.rollList, iRollDao[it.noteEntity.id])
        }
    }

    @Test fun getView() = inRoom {
        noteFirst.let {
            insertRollRelation(it)
            assertEquals(
                    it.rollList.filter { roll -> roll.position < 4 },
                    iRollDao.getView(it.noteEntity.id)
            )
        }

        noteSecond.let {
            insertRollRelation(it)
            assertEquals(
                    it.rollList.filter { roll -> roll.position < 4 },
                    iRollDao.getView(it.noteEntity.id)
            )
        }
    }

    private companion object {
        val noteFirst = NoteModel(NoteEntity(
                id = 1, create = DATE_1, change = DATE_2, type = NoteType.ROLL
        ), arrayListOf(
                RollEntity(id = 1, noteId = 1, position = 0, isCheck = false, text = "01234"),
                RollEntity(id = 2, noteId = 1, position = 1, isCheck = true, text = "12345"),
                RollEntity(id = 3, noteId = 1, position = 2, isCheck = false, text = "23456"),
                RollEntity(id = 4, noteId = 1, position = 3, isCheck = true, text = "34567")
        ))

        val noteSecond = NoteModel(NoteEntity(
                id = 2, create = DATE_3, change = DATE_4, type = NoteType.ROLL
        ), arrayListOf(
                RollEntity(id = 5, noteId = 2, position = 0, isCheck = false, text = "01234"),
                RollEntity(id = 6, noteId = 2, position = 1, isCheck = true, text = "12345"),
                RollEntity(id = 7, noteId = 2, position = 2, isCheck = false, text = "23456"),
                RollEntity(id = 8, noteId = 2, position = 3, isCheck = true, text = "34567")
        ))
    }

}