package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IRollVisibleDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.ParentRoomTest

/**
 * Integration test for [IRollVisibleDao].
 */
@RunWith(AndroidJUnit4::class)
class RollVisibleDaoTest : ParentRoomTest() {

    @Test fun insertWithUnique() = inRoomTest {
        noteDao.insert(noteEntity)

        assertEquals(1, rollVisibleDao.insert(entity))
        assertEquals(RoomDb.UNIQUE_ERROR_ID, rollVisibleDao.insert(entity))
    }

    @Test fun update() = inRoomTest {
        noteDao.insert(noteEntity)

        rollVisibleDao.insert(entity)
        assertEquals(false, rollVisibleDao.get(entity.noteId))

        rollVisibleDao.update(entity.noteId, true)
        assertEquals(true, rollVisibleDao.get(entity.noteId))
    }

    @Test fun get_byId() = inRoomTest {
        noteDao.insert(noteEntity)

        assertNull(rollVisibleDao.get(entity.noteId))

        rollVisibleDao.insert(entity)
        assertEquals(false, rollVisibleDao.get(entity.noteId))
    }

    @Test fun get_byIdList() = inRoomTest {
        TODO()
    }

    private companion object {
        val noteEntity = NoteEntity(id = 1,
                create = DATE_5, change = DATE_3, name = "NAME 1", text = "3/5", color = 0,
                type = NoteType.TEXT, rankId = -1, rankPs = -1, isBin = false, isStatus = true
        )

        val entity = RollVisibleEntity(id = 1, noteId = 1, value = false)
    }

}