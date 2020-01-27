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

    private suspend fun RoomDb.insertRollRelation(model: Model) = with(model) {
        noteDao.insert(entity)
        rollList.forEach { rollDao.insert(it) }
    }


    @Test fun insertWithUnique() = inRoomTest {
        insertRollRelation(modelFirst)

        with(modelFirst) {
            rollList.forEach { item -> assertEquals(UNIQUE_ERROR_ID, rollDao.insert(item)) }
            assertEquals(rollList, rollDao.get(entity.id))
        }
    }

    @Test fun update() = inRoomTest {
        insertRollRelation(modelSecond)

        with(modelSecond) {
            rollList[0].copy(position = 4, isCheck = true, text = "00000").let {
                rollDao.update(it.id!!, it.position, it.text)
                rollDao.update(it.id!!, it.isCheck)

                assertTrue(rollDao.get(entity.id).contains(it))
            }
        }
    }

    @Test fun updateCheck() = inRoomTest {
        insertRollRelation(modelSecond)

        with(modelSecond) {
            rollDao.updateAllCheck(entity.id, check = true)
            assertEquals(copy().rollList.apply {
                forEach { it.isCheck = true }
            }, rollDao.get(entity.id))
        }
    }

    @Test fun deleteAfterSwipe() = inRoomTest {
        insertRollRelation(modelFirst)

        with(modelFirst) {
            val listSave = rollList.filter { it.isCheck }

            entity.id.let { id ->
                rollDao.delete(id, listSave.map { it.id ?: -1 })
                assertEquals(listSave, rollDao.get(id))
            }
        }
    }

    @Test fun deleteAll() = inRoomTest {
        insertRollRelation(modelFirst)

        modelFirst.entity.id.let {
            rollDao.delete(it)
            assertTrue(rollDao.get(it).isEmpty())
        }
    }


    @Test fun get() = inRoomTest {
        modelFirst.let {
            insertRollRelation(it)
            assertEquals(it.rollList, rollDao.get(it.entity.id))
        }

        modelSecond.let {
            insertRollRelation(it)
            assertEquals(it.rollList, rollDao.get(it.entity.id))
        }
    }

    @Test fun getView() = inRoomTest {
        modelFirst.let {
            insertRollRelation(it)
            assertEquals(
                    it.rollList.filter { roll -> roll.position < 4 },
                    rollDao.getView(it.entity.id)
            )
        }

        modelSecond.let {
            insertRollRelation(it)
            assertEquals(
                    it.rollList.filter { roll -> roll.position < 4 },
                    rollDao.getView(it.entity.id)
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