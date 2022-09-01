package sgtmelon.scriptum.integrational.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.math.abs
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RankDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.test.common.nextString

/**
 * Integration test for [RankDao] and safe functions.
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class RankDaoTest : ParentRoomTest() {

    //region Variables

    private val firstRank = RankEntity(id = 1, position = 1, name = "123", isVisible = true)
    private val secondRank = RankEntity(id = 2, position = 0, name = "234", isVisible = false)
    private val thirdRank = RankEntity(id = 3, position = 2, name = "345", isVisible = true)

    private val list get() = listOf(firstRank, secondRank, thirdRank).shuffled()

    //endregion

    //region Help functions

    private suspend fun Database.insert(rank: RankEntity) {
        rankDao.insert(rank)
        assertEquals(rankDao.get(rank.id), rank)
    }

    //endregion

    override fun setUp() {
        super.setUp()

        assertNotEquals(firstRank.name, secondRank.name)
        assertNotEquals(secondRank.name, thirdRank.name)
        assertNotEquals(thirdRank.name, firstRank.name)
    }

    @Test fun insert() = inRoomTest { insert(firstRank) }

    /**
     * Check OnConflictStrategy.IGNORE on inserting with same [RankEntity.id].
     */
    @Test fun insertWithSameId() = inRoomTest {
        insert(firstRank)

        val conflict = firstRank.copy(isVisible = !firstRank.isVisible)
        assertEquals(rankDao.insert(conflict), DaoConst.UNIQUE_ERROR_ID)

        assertEquals(rankDao.get(firstRank.id), firstRank)
    }

    /**
     * Check what every [RankEntity.name] must be unique.
     */
    @Test fun insertWithNoteIdUnique() = inRoomTest {
        insert(firstRank)

        val unique = secondRank.copy(name = firstRank.name)
        assertEquals(rankDao.insert(unique), DaoConst.UNIQUE_ERROR_ID)

        assertEquals(rankDao.get(firstRank.id), firstRank)
    }

    @Test fun insertSafe() = inRoomTest {
        assertEquals(rankDao.insertSafe(firstRank), firstRank.id)
        assertNull(rankDao.insertSafe(secondRank.copy(name = firstRank.name)))
    }

    @Test fun delete() = inRoomTest {
        insert(firstRank)
        rankDao.delete(firstRank.name)
        assertNull(rankDao.get(firstRank.id))
    }

    @Test fun updateItemWithConflict() = inRoomTest { rankDao.update(firstRank) }

    @Test fun updateItem() = inRoomTest {
        insert(firstRank)

        val update = firstRank.copy(name = nextString())
        rankDao.update(update)

        assertEquals(rankDao.get(update.id), update)
    }

    @Test fun updateListWithConflict() = inRoomTest { rankDao.update(list) }

    @Test fun updateList() = inRoomTest {
        val list = list

        for (entity in list) {
            insert(entity)
        }

        val updateList = list.map { it.copy(name = nextString()) }

        for (entity in updateList) {
            assertTrue(updateList.filter { it.name == entity.name }.size == 1)
        }

        rankDao.update(updateList)

        for (entity in updateList) {
            assertEquals(rankDao.get(entity.id), entity)
        }
    }

    @Test fun getCount() = inRoomTest {
        val list = list

        for (entity in list) {
            insert(entity)
        }

        assertEquals(rankDao.getCount(), list.size)
    }

    @Test fun get() = inRoomTest {
        assertNull(rankDao.get(abs(Random.nextLong())))

        val entity = list.random()
        insert(entity)
        assertEquals(rankDao.get(entity.id), entity)
    }

    @Test fun getList() = inRoomTest {
        val list = list

        for (entity in list) {
            insert(entity)
        }

        val resultList = list.sortedBy { it.position }
        assertEquals(rankDao.getList(), resultList)
    }

    @Test fun getIdVisibleList() = inRoomTest {
        val list = list

        for (entity in list) {
            insert(entity)
        }

        val resultList = list.asSequence()
            .filter { it.isVisible }
            .sortedBy { it.position }
            .map { it.id }
            .toList()

        assertEquals(rankDao.getIdVisibleList(), resultList)
    }

    @Test fun getIdList() = inRoomTest {
        val list = list

        for (entity in list) {
            insert(entity)
        }

        val resultList = list.sortedBy { it.position }.map { it.id }
        assertEquals(rankDao.getIdList(), resultList)
    }

    @Test fun getNameList() = inRoomTest {
        val list = list

        for (entity in list) {
            insert(entity)
        }

        val resultList = list.sortedBy { it.position }.map { it.name }
        assertEquals(rankDao.getNameList(), resultList)
    }

    @Test fun getId() = inRoomTest {
        assertNull(rankDao.getId(abs(Random.nextInt())))

        val entity = list.random()
        insert(entity)

        assertEquals(rankDao.getId(entity.position), entity.id)
    }

    //region Clean up

    //    private val firstRank = RankEntity(id = 1, noteId = arrayListOf(), position = 1, name = "123")
    //
    //    private val secondRank = RankEntity(
    //        id = 2, noteId = arrayListOf(), position = 0, name = "234", isVisible = false
    //    )
    //
    //    private val thirdRank = RankEntity(id = 3, noteId = arrayListOf(), position = 2, name = "345")
    //
    //    private fun inRankDao(func: suspend IRankDao.() -> Unit) = inRoomTest {
    //        rankDao.apply { func() }
    //    }
    //
    //    private suspend fun IRankDao.insertAll(): List<RankEntity> {
    //        return arrayListOf(firstRank, secondRank, thirdRank).apply {
    //            for (it in this) {
    //                assertNotNull(safeInsert(it))
    //            }
    //
    //            sortBy { it.position }
    //        }
    //    }
    //
    //    // Dao common functions
    //
    //    @Test fun insertWithUnique() = inRankDao {
    //        assertEquals(1, insert(firstRank))
    //
    //        val sameId = secondRank.copy(id = firstRank.id)
    //
    //        assertEquals(DaoConst.UNIQUE_ERROR_ID, insert(sameId))
    //        assertNull(safeInsert(sameId))
    //
    //        val sameName = secondRank.copy(name = firstRank.name)
    //
    //        assertEquals(DaoConst.UNIQUE_ERROR_ID, insert(sameName))
    //    }
    //
    //    @Test fun delete() = inRankDao {
    //        assertNotNull(safeInsert(firstRank))
    //        delete(firstRank.name)
    //        assertNull(get(firstRank.id))
    //    }
    //
    //    @Test fun update() = inRankDao {
    //        assertNotNull(safeInsert(firstRank))
    //
    //        firstRank.copy(name = "12345", isVisible = false).let {
    //            update(it)
    //            assertEquals(it, get(it.id))
    //        }
    //    }
    //
    //    @Test fun updateByList() = inRankDao {
    //        assertNotNull(safeInsert(firstRank))
    //        assertNotNull(safeInsert(secondRank))
    //
    //        val updateList = arrayListOf(firstRank.copy(position = 0), secondRank.copy(position = 1))
    //
    //        update(updateList)
    //
    //        for (it in updateList) {
    //            assertEquals(it, get(it.id))
    //        }
    //    }
    //
    //    @Test fun updateWithUnique() = inRankDao {
    //        assertNotNull(safeInsert(firstRank))
    //        assertNotNull(safeInsert(secondRank))
    //
    //        secondRank.copy(id = firstRank.id).let {
    //            update(it)
    //            assertEquals(secondRank, get(secondRank.id))
    //
    //            update(arrayListOf(firstRank, it))
    //            assertEquals(secondRank, get(secondRank.id))
    //        }
    //
    //        secondRank.copy(name = firstRank.name).let {
    //            update(it)
    //            assertEquals(secondRank, get(secondRank.id))
    //
    //            update(arrayListOf(firstRank, it))
    //            assertEquals(secondRank, get(secondRank.id))
    //        }
    //    }
    //
    //    // Dao get functions
    //
    //    @Test fun getCount() = inRankDao {
    //        assertEquals(insertAll().size, getCount())
    //    }
    //
    //    @Test fun getOnWrongId() = inRankDao { assertNull(get(Random.nextLong())) }
    //
    //    @Test fun getOnCorrectId() = inRankDao {
    //        insertAll()
    //
    //        assertEquals(secondRank, get(secondRank.id))
    //        assertEquals(thirdRank, get(thirdRank.id))
    //    }
    //
    //    @Test fun getList() = inRankDao { assertEquals(insertAll(), get()) }
    //
    //    @Test fun getIdVisibleList() = inRankDao {
    //        assertEquals(insertAll().filter { it.isVisible }.map { it.id }, getIdVisibleList())
    //    }
    //
    //    @Test fun getIdList() = inRankDao {
    //        assertEquals(insertAll().map { it.id }, getIdList())
    //    }
    //
    //    @Test fun getNameList() = inRankDao {
    //        assertEquals(insertAll().map { it.name }, getNameList())
    //    }
    //
    //    @Test fun getId() = inRankDao {
    //        assertNull(getId(Random.nextInt()))
    //
    //        for ((i, id) in insertAll().map { it.id }.withIndex()) {
    //            assertEquals(id, getId(i))
    //        }
    //    }

    //endregion
}