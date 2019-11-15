package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.room.dao.IRollDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [IRollDao]
 */
@RunWith(AndroidJUnit4::class)
class RollDaoTest : ParentIntegrationTest() {

    private fun RoomDb.insertRollRelation(model: Model) = with(model) {
        iNoteDao.insert(entity)
        rollList.forEach { iRollDao.insert(it) }
    }

    @Test fun insertWithUnique() = inRoom {
        insertRollRelation(modelFirst)

        with(modelFirst) {
            rollList.forEach { item -> iRollDao.insert(item) }
            assertEquals(rollList, iRollDao[entity.id])
        }
    }

    @Test fun update() = inRoom {
        insertRollRelation(modelSecond)

        with(modelSecond) {
            rollList[0].copy(position = 4, isCheck = true, text = "00000").let {
                iRollDao.update(it.id!!, it.position, it.text)
                iRollDao.update(it.id!!, it.isCheck)

                assertTrue(iRollDao[entity.id].contains(it))
            }
        }
    }

    @Test fun updateCheck() = inRoom {
        insertRollRelation(modelSecond)

        with(modelSecond) {
            iRollDao.updateAllCheck(entity.id, check = true)
            assertEquals(copy().rollList.apply {
                forEach { it.isCheck = true }
            }, iRollDao[entity.id])
        }
    }

    @Test fun delete() = inRoom {
        insertRollRelation(modelFirst)

        with(modelFirst) {
            val listSave = rollList.filter { it.isCheck }

            entity.id.let { id ->
                iRollDao.delete(id, listSave.map { it.id ?: -1 })
                assertEquals(listSave, iRollDao[id])
            }
        }
    }

    @Test fun deleteAll() = inRoom {
        insertRollRelation(modelFirst)

        modelFirst.entity.id.let {
            iRollDao.delete(it)
            assertTrue(iRollDao[it].isEmpty())
        }
    }

    @Test fun get() = inRoom {
        modelFirst.let {
            insertRollRelation(it)
            assertEquals(it.rollList, iRollDao[it.entity.id])
        }

        modelSecond.let {
            insertRollRelation(it)
            assertEquals(it.rollList, iRollDao[it.entity.id])
        }
    }

    @Test fun getView() = inRoom {
        modelFirst.let {
            insertRollRelation(it)
            assertEquals(
                    it.rollList.filter { roll -> roll.position < 4 },
                    iRollDao.getView(it.entity.id)
            )
        }

        modelSecond.let {
            insertRollRelation(it)
            assertEquals(
                    it.rollList.filter { roll -> roll.position < 4 },
                    iRollDao.getView(it.entity.id)
            )
        }
    }

    private data class Model(val entity: NoteEntity, val rollList: List<RollEntity>)

    private companion object {
        val modelFirst = Model(NoteEntity(
                id = 1, create = DATE_1, change = DATE_2, type = NoteType.ROLL
        ), arrayListOf(
                RollEntity(id = 1, noteId = 1, position = 0, isCheck = false, text = "01234"),
                RollEntity(id = 2, noteId = 1, position = 1, isCheck = true, text = "12345"),
                RollEntity(id = 3, noteId = 1, position = 2, isCheck = false, text = "23456"),
                RollEntity(id = 4, noteId = 1, position = 3, isCheck = true, text = "34567")
        ))

        val modelSecond = Model(NoteEntity(
                id = 2, create = DATE_3, change = DATE_4, type = NoteType.ROLL
        ), arrayListOf(
                RollEntity(id = 5, noteId = 2, position = 0, isCheck = false, text = "01234"),
                RollEntity(id = 6, noteId = 2, position = 1, isCheck = true, text = "12345"),
                RollEntity(id = 7, noteId = 2, position = 2, isCheck = false, text = "23456"),
                RollEntity(id = 8, noteId = 2, position = 3, isCheck = true, text = "34567")
        ))
    }

}