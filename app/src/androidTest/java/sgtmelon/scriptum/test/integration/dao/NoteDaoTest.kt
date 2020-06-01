package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.room.RoomDb
import sgtmelon.scriptum.data.room.dao.INoteDao
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [INoteDao]
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentIntegrationTest() {

    private fun inNoteDao(func: suspend INoteDao.() -> Unit) = inRoomTest { noteDao.apply { func() } }

    private suspend fun INoteDao.insertAllTo(bin: Boolean) {
        insert(firstNote.copy(isBin = bin))
        insert(secondNote.copy(isBin = bin))
        insert(thirdNote.copy(isBin = bin))

        assertNotNull(get(firstNote.id))
        assertNotNull(get(secondNote.id))
        assertNotNull(get(thirdNote.id))
    }

    private suspend fun INoteDao.updateAllTo(bin: Boolean) {
        update(firstNote.copy(isBin = bin))
        update(secondNote.copy(isBin = bin))
        update(thirdNote.copy(isBin = bin))
    }


    @Test fun insertWithUnique() = inNoteDao {
        assertEquals(1, insert(firstNote))
        assertEquals(RoomDb.UNIQUE_ERROR_ID, insert(firstNote))
    }

    @Test fun delete() = inNoteDao {
        insert(firstNote)
        assertEquals(firstNote, get(firstNote.id))

        delete(firstNote)
        assertNull(get(firstNote.id))
    }

    @Test fun deleteByList() = inNoteDao {
        insertAllTo(bin = false)

        val list = listOf(firstNote, secondNote, thirdNote)

        delete(list)
        list.forEach { assertNull(get(it.id)) }
    }

    @Test fun update() = inNoteDao {
        insert(firstNote)
        assertEquals(firstNote, get(firstNote.id))

        firstNote.copy(color = 10, isBin = true).let {
            update(it)
            assertEquals(it, get(firstNote.id))
        }
    }

    @Test fun updateByList() = inNoteDao {
        insert(firstNote)
        insert(thirdNote)
        assertEquals(arrayListOf(firstNote, thirdNote), getByColor(bin = false))

        firstNote.copy(color = 3).let { first ->
            thirdNote.copy(color = 2).let { third ->
                update(arrayListOf(first, third))
                assertEquals(arrayListOf(third, first), getByColor(bin = false))
            }
        }
    }


    @Test fun getCount() = inNoteDao {
        assertEquals(0, getCount(bin = false, rankIdList = listOf()))
        assertEquals(0, getCount(bin = true, rankIdList = listOf()))

        insert(firstNote)
        insert(secondNote)
        insert(thirdNote)

        assertEquals(1, getCount(bin = false, rankIdList = listOf()))
        assertEquals(2, getCount(bin = false, rankIdList = listOf(2)))
        assertEquals(1, getCount(bin = true, rankIdList = listOf(1)))
    }

    @Test fun getByWrongId() = inNoteDao { assertNull(get(Random.nextLong())) }

    @Test fun getByCorrectId() = inNoteDao {
        insert(firstNote)
        assertEquals(firstNote, get(firstNote.id))

        insert(secondNote)
        assertEquals(secondNote, get(secondNote.id))

        insert(thirdNote)
        assertEquals(thirdNote, get(thirdNote.id))
    }

    @Test fun getByIdList() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(listOf(firstNote, thirdNote), get(listOf(
                firstNote.id, thirdNote.id, Random.nextLong()
        )))

        assertEquals(listOf(secondNote.copy(isBin = false), thirdNote), get(listOf(
                secondNote.id, thirdNote.id
        )))
    }

    @Test fun getByBin() = inNoteDao {
        insert(firstNote)
        insert(secondNote)
        insert(thirdNote)

        assertEquals(listOf(firstNote, thirdNote), get(bin = false))
        assertEquals(listOf(secondNote), get(bin = true))
    }


    @Test fun getByChange() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                thirdNote, secondNote.copy(isBin = false), firstNote
        ), getByChange(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                thirdNote.copy(isBin = true), secondNote, firstNote.copy(isBin = true)
        ), getByChange(bin = true))
    }

    @Test fun getByCreate() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                thirdNote, secondNote.copy(isBin = false), firstNote
        ), getByCreate(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                thirdNote.copy(isBin = true), secondNote, firstNote.copy(isBin = true)
        ), getByCreate(bin = true))
    }

    @Test fun getByRank() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                firstNote, secondNote.copy(isBin = false), thirdNote
        ), getByRank(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                firstNote.copy(isBin = true), secondNote, thirdNote.copy(isBin = true)
        ), getByRank(bin = true))
    }

    @Test fun getByColor() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                secondNote.copy(isBin = false), firstNote, thirdNote
        ), getByColor(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                secondNote, firstNote.copy(isBin = true), thirdNote.copy(isBin = true)
        ), getByColor(bin = true))
    }


    private companion object {
        val firstNote = NoteEntity(
                id = 1, create = DATE_1, change = DATE_2, text = "123", name = "456",
                color = 1, type = NoteType.TEXT, rankId = -1, rankPs = -1, isBin = false
        )

        val secondNote = NoteEntity(
                id = 2, create = DATE_2, change = DATE_3, text = "654", name = "321",
                color = 1, type = NoteType.TEXT, rankId = 1, rankPs = 1, isBin = true
        )

        val thirdNote = NoteEntity(
                id = 3, create = DATE_3, change = DATE_4, text = "123", name = "",
                color = 3, type = NoteType.TEXT, rankId = 2, rankPs = 2, isBin = false
        )
    }

}