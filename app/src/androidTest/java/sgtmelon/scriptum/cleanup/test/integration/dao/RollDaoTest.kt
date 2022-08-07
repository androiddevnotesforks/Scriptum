package sgtmelon.scriptum.cleanup.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.dao.IRollDao
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.cleanup.data.room.extension.safeDelete
import sgtmelon.scriptum.cleanup.data.room.extension.safeDeleteByList
import sgtmelon.scriptum.cleanup.data.room.extension.safeGet
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_3
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_4
import sgtmelon.scriptum.parent.provider.DateProvider.nextDate
import sgtmelon.test.common.nextString

/**
 * Integration test for [IRollDao]
 */
@RunWith(AndroidJUnit4::class)
class RollDaoTest : ParentRoomTest() {

    //region Variables

    private data class Model(val entity: NoteEntity, val rollList: List<RollEntity>)

    private fun getRandomRoll(id: Long, position: Int, noteId: Long): RollEntity {
        return RollEntity(id, noteId, position, Random.nextBoolean(), nextString())
    }

    private val firstModel = Model(
        NoteEntity(id = 1, create = DATE_1, change = DATE_2, type = NoteType.ROLL),
        List(size = 20) { getRandomRoll(it.toLong(), it, noteId = 1) }
    )

    private val secondModel = Model(
        NoteEntity(id = 2, create = DATE_3, change = DATE_4, type = NoteType.ROLL),
        List(size = 20) { getRandomRoll((firstModel.rollList.size + it).toLong(), it, noteId = 2) }
    )

    //endregion

    private suspend fun RoomDb.insertRollRelation(model: Model) = with(model) {
        noteDao.insert(entity)
        for (it in rollList.asReversed()) {
            rollDao.insert(it)
        }
    }

    // Dao common functions

    @Test fun insertWithUnique() = inRoomTest {
        insertRollRelation(firstModel)

        with(firstModel) {
            for (it in rollList) {
                assertEquals(DaoConst.UNIQUE_ERROR_ID, rollDao.insert(it))
            }

            assertEquals(rollList, rollDao.get(entity.id))
        }
    }

    @Test fun update() = inRoomTest {
        insertRollRelation(secondModel)

        with(secondModel) {
            rollList[0].copy(position = 4, isCheck = true, text = "00000").let {
                rollDao.update(it.id!!, it.position, it.text)
                rollDao.update(it.id!!, it.isCheck)

                assertTrue(rollDao.get(entity.id).contains(it))
            }
        }
    }

    @Test fun updateCheck() = inRoomTest {
        insertRollRelation(secondModel)

        with(secondModel) {
            rollDao.updateAllCheck(entity.id, check = true)

            val newList = copy().rollList.apply { for (it in this) it.isCheck = true }
            assertEquals(newList, rollDao.get(entity.id))
        }
    }

    @Test fun deleteAfterSwipe() = inRoomTest {
        insertRollRelation(firstModel)

        val saveList = firstModel.rollList.filter { it.isCheck }
        val id = firstModel.entity.id

        rollDao.delete(id, saveList.map { it.id!! })
        assertEquals(saveList, rollDao.get(id))
    }

    @Test fun deleteByList() = inRoomTest {
        insertRollRelation(firstModel)

        val rollList = firstModel.rollList
        val id = firstModel.entity.id

        val filterValue = Random.nextBoolean()
        val deleteList = ArrayList(rollList.filter { it.isCheck == filterValue })
        val saveList = ArrayList(rollList).apply { removeAll(deleteList) }

        rollDao.deleteByList(id, deleteList.map { it.id!! })
        assertEquals(saveList, rollDao.get(id))
    }

    @Test fun deleteAll() = inRoomTest {
        insertRollRelation(firstModel)

        val id = firstModel.entity.id
        rollDao.delete(id)
        assertTrue(rollDao.get(id).isEmpty())
    }

    @Test fun deleteCrowd() = inRoomTest {
        val noteId = firstModel.entity.id
        val rollList = List(CROWD_SIZE) { getRandomRoll(it.toLong(), it, noteId) }
        val model = firstModel.copy(rollList = rollList)

        insertRollRelation(model)

        val saveList = model.rollList.filter { it.isCheck }

        rollDao.safeDelete(noteId, saveList.map { it.id!! })

        assertEquals(saveList, rollDao.get(noteId))
    }

    @Test fun deleteByListCrowd() = inRoomTest {
        val noteId = firstModel.entity.id
        val rollList = List(CROWD_SIZE) { getRandomRoll(it.toLong(), it, noteId) }
        val model = firstModel.copy(rollList = rollList)

        insertRollRelation(model)

        val filterValue = Random.nextBoolean()
        val deleteList = ArrayList(rollList.filter { it.isCheck == filterValue })
        val saveList = ArrayList(rollList).apply { removeAll(deleteList.toSet()) }

        rollDao.safeDeleteByList(noteId, deleteList.map { it.id!! })

        assertEquals(saveList, rollDao.get(noteId))
    }

    // Dao get functions

    @Test fun get() = inRoomTest {
        insertRollRelation(secondModel)
        insertRollRelation(firstModel)

        val expectedList = mutableListOf<RollEntity>()
        expectedList.addAll(firstModel.rollList)
        expectedList.addAll(secondModel.rollList)

        assertEquals(expectedList, rollDao.get())
    }

    @Test fun getById() = inRoomTest {
        firstModel.let {
            insertRollRelation(it)
            assertEquals(it.rollList, rollDao.get(it.entity.id))
        }

        secondModel.let {
            insertRollRelation(it)
            assertEquals(it.rollList, rollDao.get(it.entity.id))
        }
    }

    @Test fun getByIdList() = inRoomTest {
        insertRollRelation(firstModel)
        insertRollRelation(secondModel)

        val noteIdList = listOf(firstModel.entity.id, secondModel.entity.id)
        val resultList = rollDao.get(noteIdList)

        val insertList = mutableListOf<RollEntity>()
        insertList.addAll(firstModel.rollList)
        insertList.addAll(secondModel.rollList)

        assertEquals(insertList.size, resultList.size)
        assertTrue(insertList.containsAll(resultList))
        assertTrue(resultList.containsAll(insertList))
    }

    @Test fun getByIdListCrowd() = inRoomTest {
        // TODO test this safeGet through simple get func (first half of func)
        /**
         * Roll id must be unique. So that is why this variable exist.
         */
        var rollId = 1L
        val modelList = List(CROWD_SIZE) {
            val noteId = it.toLong() + 1
            val list = List(size = (2..4).random()) { index ->
                getRandomRoll(rollId++, index, noteId)
            }

            return@List Model(
                NoteEntity(noteId, nextDate(), nextDate(), type = NoteType.ROLL),
                list
            )
        }

        /**
         * Insert our modelList inside data base.
         */
        for (model in modelList) {
            insertRollRelation(model)
        }

        val resultRollGetList = rollDao.safeGet(modelList.map { it.entity.id })
        val rollGetList = modelList.map { it.rollList }.flatten()

        assertEquals(rollGetList.size, resultRollGetList.size)
        assertTrue(rollGetList.containsAll(resultRollGetList))
        assertTrue(resultRollGetList.containsAll(rollGetList))

        TODO()
    }

    @Test fun getView() = inRoomTest {
        firstModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> roll.position < 4 },
                rollDao.getView(it.entity.id)
            )
        }

        secondModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> roll.position < 4 },
                rollDao.getView(it.entity.id)
            )
        }
    }

    @Test fun getViewHide() = inRoomTest {
        firstModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> !roll.isCheck }.take(n = 4),
                rollDao.getViewHide(it.entity.id)
            )
        }

        secondModel.let {
            insertRollRelation(it)
            assertEquals(
                it.rollList.filter { roll -> !roll.isCheck }.take(n = 4),
                rollDao.getViewHide(it.entity.id)
            )
        }
    }
}