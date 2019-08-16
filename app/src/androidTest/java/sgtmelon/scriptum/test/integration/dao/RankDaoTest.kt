package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.room.dao.IRankDao
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [IRankDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RankDaoTest : ParentIntegrationTest() {

    private fun inRankDao(func: IRankDao.() -> Unit) = inRoom { iRankDao.apply(func) }

    private fun IRankDao.insertAll(): List<RankEntity> =
            arrayListOf(rankFirst, rankSecond, rankThird).apply {
                forEach { insert(it) }
                sortBy { it.position }
            }

    @Test fun insertWithUnique() = inRankDao {
        insert(rankFirst)

        insert(rankSecond.copy(id = rankFirst.id))
        assertTrue(getCount() == 1)

        insert(rankSecond.copy(name = rankFirst.name))
        assertTrue(getCount() == 1)
    }

    @Test fun delete() = inRankDao {
        insert(rankFirst)
        delete(rankFirst.name)
        assertNull(get(rankFirst.name))
    }

    @Test fun update() = inRankDao {
        insert(rankFirst)

        rankFirst.copy(name = "12345", isVisible = false).let {
            update(it)
            assertEquals(it, get(it.name))
        }
    }

    @Test fun updateByList() = inRankDao {
        insert(rankFirst)
        insert(rankSecond)

        val updateList = arrayListOf(rankFirst.copy(position = 0), rankSecond.copy(position = 1))

        update(updateList)
        updateList.forEach { assertEquals(it, get(it.id)) }
    }

    @Test fun updateWithUnique() = inRankDao {
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

    @Test fun getOnWrongId() = inRankDao { assertNull(get(data.uniqueString)) }

    @Test fun getOnCorrectId() = inRankDao {
        insert(rankFirst)
        assertEquals(rankFirst, get(rankFirst.name))

        insert(rankSecond)
        assertEquals(rankSecond, get(rankSecond.name))

        insert(rankThird)
        assertEquals(rankThird, get(rankThird.name))
    }

    @Test fun getList() = inRankDao { assertEquals(insertAll(), get()) }

    @Test fun getById() = inRankDao {
        insertAll()

        assertEquals(rankSecond, get(rankSecond.id))
        assertEquals(rankThird, get(rankThird.id))
    }

    @Test fun getIdVisibleList() = inRankDao {
        assertEquals(insertAll().filter { it.isVisible }.map { it.id }, getIdVisibleList())
    }

    @Test fun getCount() = inRankDao {
        assertEquals(insertAll().size, getCount())
    }

    @Test fun getNameList() = inRankDao {
        assertEquals(insertAll().map { it.name }, getNameList())
    }

    @Test fun getIdList() = inRankDao {
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