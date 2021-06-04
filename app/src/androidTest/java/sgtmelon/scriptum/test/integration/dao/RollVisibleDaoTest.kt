package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.IRollVisibleDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.data.room.extension.inRoomTest
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.parent.ParentRoomTest

/**
 * Integration test for [IRollVisibleDao].
 */
@RunWith(AndroidJUnit4::class)
class RollVisibleDaoTest : ParentRoomTest() {

    //region Variables

    private data class Model(val noteEntity: NoteEntity, val entity: RollVisibleEntity)

    private val firstModel = Model(
        NoteEntity(
            id = 1,
            create = DATE_5, change = DATE_3, name = "NAME 1", text = "TEXT 1", color = 0,
            type = NoteType.TEXT, rankId = -1, rankPs = -1, isBin = false, isStatus = true
        ), RollVisibleEntity(id = 1, noteId = 1, value = false)
    )

    private val secondModel = Model(
        NoteEntity(
            id = 2,
            create = DATE_1, change = DATE_2, name = "NAME 2", text = "3/5", color = 1,
            type = NoteType.ROLL, rankId = 10, rankPs = 1, isBin = true, isStatus = false
        ), RollVisibleEntity(id = 2, noteId = 2, value = false)
    )

    //endregion

    // Dao functions

    @Test fun insertWithUnique() = inRoomTest {
        firstModel.let {
            noteDao.insert(it.noteEntity)

            assertEquals(1, rollVisibleDao.insert(it.entity))
            assertEquals(RoomDb.UNIQUE_ERROR_ID, rollVisibleDao.insert(it.entity))
        }
    }

    @Test fun update() = inRoomTest {
        firstModel.let {
            noteDao.insert(it.noteEntity)

            rollVisibleDao.insert(it.entity)
            assertEquals(false, rollVisibleDao.get(it.entity.noteId))

            rollVisibleDao.update(it.entity.noteId, true)
            assertEquals(true, rollVisibleDao.get(it.entity.noteId))
        }
    }

    @Test fun get() = inRoomTest {
        noteDao.insert(firstModel.noteEntity)
        noteDao.insert(secondModel.noteEntity)

        rollVisibleDao.insert(firstModel.entity)
        rollVisibleDao.insert(secondModel.entity)

        assertEquals(listOf(firstModel.entity, secondModel.entity), rollVisibleDao.get())
    }

    @Test fun getById() = inRoomTest {
        firstModel.let {
            noteDao.insert(it.noteEntity)

            assertNull(rollVisibleDao.get(it.entity.noteId))

            rollVisibleDao.insert(it.entity)
            assertEquals(false, rollVisibleDao.get(it.entity.noteId))
        }
    }

    @Test fun getByIdList() = inRoomTest {
        noteDao.insert(firstModel.noteEntity)
        noteDao.insert(secondModel.noteEntity)

        rollVisibleDao.insert(firstModel.entity)
        rollVisibleDao.insert(secondModel.entity)

        val noteIdList = listOf(firstModel.noteEntity.id, secondModel.noteEntity.id)
        val resultList = rollVisibleDao.get(noteIdList)

        assertEquals(2, resultList.size)
        assertTrue(resultList.contains(firstModel.entity))
        assertTrue(resultList.contains(secondModel.entity))
    }

    @Test fun getByIdListCrowd() = inRoomTest { rollVisibleDao.get(crowdList) }

}