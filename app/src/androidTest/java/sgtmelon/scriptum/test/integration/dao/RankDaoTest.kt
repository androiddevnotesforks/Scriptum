package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.room.dao.RankDao
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [RankDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RankDaoTest : ParentIntegrationTest() {

    private fun inTheRankDao(func: RankDao.() -> Unit) = inTheRoom { getRankDao().apply(func) }

    private fun RankDao.insertAll(): List<RankEntity> =
            arrayListOf(rankFirst, rankSecond, rankThird).apply {
                forEach { insert(it) }
                sortBy { it.position }
            }

    @Test fun insertWithWithUnique() = inTheRankDao {
        insert(rankFirst)

        insert(rankSecond.copy(id = rankFirst.id))
        assertTrue(getCount() == 1)

        insert(rankSecond.copy(name = rankFirst.name))
        assertTrue(getCount() == 1)
    }

    @Test fun delete() = inTheRankDao {
        insert(rankFirst)
        delete(rankFirst)
        assertNull(get(rankFirst.name))
    }

    @Test fun update() = inTheRankDao {
        insert(rankFirst)

        rankFirst.copy(name = "12345", isVisible = false).let {
            update(it)
            assertEquals(it, get(it.name))
        }
    }

    @Test fun updateByList() = inTheRankDao {
        insert(rankFirst)
        insert(rankSecond)

        val updateList = arrayListOf(rankFirst.copy(position = 0), rankSecond.copy(position = 1))

        update(updateList)
        assertEquals(updateList, get(updateList.map { it.id }))
    }

    @Test fun updateWithUnique() = inTheRankDao {
        insert(rankFirst)
        insert(rankSecond)

        rankSecond.copy(id = rankFirst.id).let {
            update(it)
            assertEquals(rankSecond, get(rankSecond.name))

            update(arrayListOf(rankFirst, it))
            assertEquals(rankSecond, get(rankSecond.name))
        }

        rankSecond.copy(name = rankFirst.name).let {
            update(it)
            assertEquals(rankSecond, get(rankSecond.name))

            update(arrayListOf(rankFirst, it))
            assertEquals(rankSecond, get(rankSecond.name))
        }
    }

    @Test fun getOnWrongId() = inTheRankDao { assertNull(get(testData.uniqueString)) }

    @Test fun getOnCorrectId() = inTheRankDao {
        insert(rankFirst)
        assertEquals(rankFirst, get(rankFirst.name))

        insert(rankSecond)
        assertEquals(rankSecond, get(rankSecond.name))

        insert(rankThird)
        assertEquals(rankThird, get(rankThird.name))
    }

    @Test fun getList() = inTheRankDao { assertEquals(insertAll(), get()) }

    @Test fun getListByIdList() = inTheRankDao {
        insertAll()

        arrayListOf(rankSecond, rankThird).let { list ->
            assertEquals(list, get(list.map { it.id }))
        }
    }

    @Test fun getIdVisibleList() = inTheRankDao {
        assertEquals(insertAll().filter { it.isVisible }.map { it.id }, getIdVisibleList())
    }

    @Test fun getCount() = inTheRankDao {
        assertEquals(insertAll().size, getCount())
    }

    @Test fun getNameList() = inTheRankDao {
        assertEquals(insertAll().map { it.name }, getNameList())
    }

    @Test fun getIdList() = inTheRankDao {
        assertEquals(insertAll().map { it.id }, getIdList())
    }

    private companion object {
        val rankFirst = RankEntity(id = 1, noteId = arrayListOf(), position = 1, name = "123")

        val rankSecond = RankEntity(
                id = 2, noteId = arrayListOf(), position = 0, name = "234", isVisible = false
        )

        val rankThird = RankEntity(id = 3, noteId = arrayListOf(), position = 2, name = "345")
    }

}