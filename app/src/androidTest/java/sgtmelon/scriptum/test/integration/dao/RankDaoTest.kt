package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

    @Test fun insertWithSameName() = inTheRankDao { TODO("") }

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

    @Test fun getOnWrongId() = inTheRankDao { assertNull(get(testData.uniqueString)) }

    @Test fun getOnCorrectId() = inTheRankDao {
        insert(rankFirst)
        assertEquals(rankFirst, get(rankFirst.name))

        insert(rankSecond)
        assertEquals(rankSecond, get(rankSecond.name))

        insert(rankThird)
        assertEquals(rankThird, get(rankThird.name))
    }

    @Test fun getList() = inTheRankDao {
        insert(rankFirst)
        insert(rankSecond)
        insert(rankThird)

        val getList = arrayListOf(rankSecond, rankThird)

        assertEquals(getList, get(getList.map { it.id }))
    }

    private companion object {
        val rankFirst = RankEntity(id = 1, noteId = arrayListOf(), position = 1, name = "123")

        val rankSecond = RankEntity(
                id = 2, noteId = arrayListOf(), position = 0, name = "234", isVisible = false
        )

        val rankThird = RankEntity(id = 3, noteId = arrayListOf(), position = 2, name = "345")
    }

}