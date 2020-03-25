package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.dao.IRankDao
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [IRankDao]
 */
@RunWith(AndroidJUnit4::class)
class RankDaoTest : ParentIntegrationTest() {

    private fun inRankDao(func: suspend IRankDao.() -> Unit) = inRoomTest {
        rankDao.apply { func() }
    }

    private suspend fun IRankDao.insertAll(): List<RankEntity> {
        return arrayListOf(rankFirst, rankSecond, rankThird).apply {
            forEach { insert(it) }
            sortBy { it.position }
        }
    }


    @Test fun insertWithUnique() = inRankDao {
        assertEquals(1, insert(rankFirst))
        assertEquals(UNIQUE_ERROR_ID, insert(rankSecond.copy(id = rankFirst.id)))
        assertEquals(UNIQUE_ERROR_ID, insert(rankSecond.copy(name = rankFirst.name)))
    }

    @Test fun delete() = inRankDao {
        insert(rankFirst)
        delete(rankFirst.name)
        assertNull(get(rankFirst.id))
    }

    @Test fun update() = inRankDao {
        insert(rankFirst)

        rankFirst.copy(name = "12345", isVisible = false).let {
            update(it)
            assertEquals(it, get(it.id))
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
            assertEquals(rankSecond, get(rankSecond.id))

            update(arrayListOf(rankFirst, it))
            assertEquals(rankSecond, get(rankSecond.id))
        }

        rankSecond.copy(name = rankFirst.name).let {
            update(it)
            assertEquals(rankSecond, get(rankSecond.id))

            update(arrayListOf(rankFirst, it))
            assertEquals(rankSecond, get(rankSecond.id))
        }
    }


    @Test fun getCount() = inRankDao {
        assertEquals(insertAll().size, getCount())
    }

    @Test fun getOnWrongId() = inRankDao { assertNull(get(Random.nextLong())) }

    @Test fun getOnCorrectId() = inRankDao {
        insertAll()

        assertEquals(rankSecond, get(rankSecond.id))
        assertEquals(rankThird, get(rankThird.id))
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

        insertAll().map { it.id }.forEachIndexed { p, id ->
            assertEquals(id, getId(p))
        }
    }

    private companion object {
        val rankFirst = RankEntity(id = 1, noteId = arrayListOf(), position = 1, name = "123")

        val rankSecond = RankEntity(
                id = 2, noteId = arrayListOf(), position = 0, name = "234", isVisible = false
        )

        val rankThird = RankEntity(id = 3, noteId = arrayListOf(), position = 2, name = "345")
    }

}