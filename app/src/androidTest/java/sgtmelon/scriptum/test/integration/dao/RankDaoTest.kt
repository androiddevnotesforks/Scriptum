package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.dao.IRankDao
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.cleanup.data.room.extension.safeInsert
import sgtmelon.scriptum.test.parent.ParentRoomTest

/**
 * Integration test for [IRankDao]
 */
@RunWith(AndroidJUnit4::class)
class RankDaoTest : ParentRoomTest() {

    private val firstRank = RankEntity(id = 1, noteId = arrayListOf(), position = 1, name = "123")

    private val secondRank = RankEntity(
        id = 2, noteId = arrayListOf(), position = 0, name = "234", isVisible = false
    )

    private val thirdRank = RankEntity(id = 3, noteId = arrayListOf(), position = 2, name = "345")

    private fun inRankDao(func: suspend IRankDao.() -> Unit) = inRoomTest {
        rankDao.apply { func() }
    }

    private suspend fun IRankDao.insertAll(): List<RankEntity> {
        return arrayListOf(firstRank, secondRank, thirdRank).apply {
            for (it in this) {
                assertNotNull(safeInsert(it))
            }

            sortBy { it.position }
        }
    }

    // Dao common functions

    @Test fun insertWithUnique() = inRankDao {
        assertEquals(1, insert(firstRank))

        val sameId = secondRank.copy(id = firstRank.id)

        assertEquals(RoomDb.UNIQUE_ERROR_ID, insert(sameId))
        assertNull(safeInsert(sameId))

        val sameName = secondRank.copy(name = firstRank.name)

        assertEquals(RoomDb.UNIQUE_ERROR_ID, insert(sameName))
    }

    @Test fun delete() = inRankDao {
        assertNotNull(safeInsert(firstRank))
        delete(firstRank.name)
        assertNull(get(firstRank.id))
    }

    @Test fun update() = inRankDao {
        assertNotNull(safeInsert(firstRank))

        firstRank.copy(name = "12345", isVisible = false).let {
            update(it)
            assertEquals(it, get(it.id))
        }
    }

    @Test fun updateByList() = inRankDao {
        assertNotNull(safeInsert(firstRank))
        assertNotNull(safeInsert(secondRank))

        val updateList = arrayListOf(firstRank.copy(position = 0), secondRank.copy(position = 1))

        update(updateList)

        for (it in updateList) {
            assertEquals(it, get(it.id))
        }
    }

    @Test fun updateWithUnique() = inRankDao {
        assertNotNull(safeInsert(firstRank))
        assertNotNull(safeInsert(secondRank))

        secondRank.copy(id = firstRank.id).let {
            update(it)
            assertEquals(secondRank, get(secondRank.id))

            update(arrayListOf(firstRank, it))
            assertEquals(secondRank, get(secondRank.id))
        }

        secondRank.copy(name = firstRank.name).let {
            update(it)
            assertEquals(secondRank, get(secondRank.id))

            update(arrayListOf(firstRank, it))
            assertEquals(secondRank, get(secondRank.id))
        }
    }

    // Dao get functions

    @Test fun getCount() = inRankDao {
        assertEquals(insertAll().size, getCount())
    }

    @Test fun getOnWrongId() = inRankDao { assertNull(get(Random.nextLong())) }

    @Test fun getOnCorrectId() = inRankDao {
        insertAll()

        assertEquals(secondRank, get(secondRank.id))
        assertEquals(thirdRank, get(thirdRank.id))
    }

    @Test fun getList() = inRankDao { assertEquals(insertAll(), get()) }

    @Test fun getIdVisibleList() = inRankDao {
        assertEquals(insertAll().filter { it.isVisible }.map { it.id }, getIdVisibleList())
    }

    @Test fun getIdList() = inRankDao {
        assertEquals(insertAll().map { it.id }, getIdList())
    }

    @Test fun getNameList() = inRankDao {
        assertEquals(insertAll().map { it.name }, getNameList())
    }

    @Test fun getId() = inRankDao {
        assertNull(getId(Random.nextInt()))

        for ((i, id) in insertAll().map { it.id }.withIndex()) {
            assertEquals(id, getId(i))
        }
    }
}