package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.dao.IRollVisibleDao
import sgtmelon.scriptum.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [IRollVisibleDao].
 */
@RunWith(AndroidJUnit4::class)
class RollVisibleDaoTest : ParentIntegrationTest() {

    private fun inRollVisibleDao(func: suspend IRollVisibleDao.() -> Unit) = inRoomTest {
        rollVisibleDao.apply { func() }
    }

    @Test fun insertWithUnique() = inRollVisibleDao {
        assertEquals(1, insert(entity))
        assertEquals(UNIQUE_ERROR_ID, insert(entity))
    }

    @Test fun update() = inRollVisibleDao {
        insert(entity)
        assertEquals(false, get(entity.noteId))

        update(entity.noteId, true)
        assertEquals(true, get(entity.noteId))
    }

    @Test fun get() = inRollVisibleDao {
        assertNull(get(entity.noteId))

        insert(entity)
        assertEquals(false, get(entity.noteId))
    }


    private companion object {
        val entity = RollVisibleEntity(id = 1, noteId = 1, value = false)
    }

}