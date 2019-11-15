package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.dao.INoteDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [INoteDao]
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentIntegrationTest() {

    private fun inNoteDao(func: INoteDao.() -> Unit) = inRoom { iNoteDao.apply(func) }

    private fun INoteDao.insertAllTo(bin: Boolean) {
        insert(noteFirst.copy(isBin = bin))
        insert(noteSecond.copy(isBin = bin))
        insert(noteThird.copy(isBin = bin))
    }

    private fun INoteDao.updateAllTo(bin: Boolean) {
        update(noteFirst.copy(isBin = bin))
        update(noteSecond.copy(isBin = bin))
        update(noteThird.copy(isBin = bin))
    }


    @Test fun insertWithUnique() = inNoteDao {
        assertEquals(1, insert(noteFirst))
        assertEquals(UNIQUE_ERROR_ID, insert(noteFirst))
    }

    @Test fun delete() = inNoteDao {
        insert(noteFirst)
        assertEquals(noteFirst, get(noteFirst.id))

        delete(noteFirst)
        assertNull(get(noteFirst.id))
    }

    @Test fun update() = inNoteDao {
        insert(noteFirst)
        assertEquals(noteFirst, get(noteFirst.id))

        noteFirst.copy(color = 10, isBin = true).let {
            update(it)
            assertEquals(it, get(noteFirst.id))
        }
    }

    @Test fun updateByList() = inNoteDao {
        insert(noteFirst)
        insert(noteThird)
        assertEquals(arrayListOf(noteFirst, noteThird), getByColor(bin = false))

        noteFirst.copy(color = 3).let { first ->
            noteThird.copy(color = 2).let { third ->
                update(arrayListOf(first, third))
                assertEquals(arrayListOf(third, first), getByColor(bin = false))
            }
        }
    }


    @Test fun getByWrongId() = inNoteDao { assertNull(get(Random.nextLong())) }

    @Test fun getByCorrectId() = inNoteDao {
        insert(noteFirst)
        assertEquals(noteFirst, get(noteFirst.id))

        insert(noteSecond)
        assertEquals(noteSecond, get(noteSecond.id))

        insert(noteThird)
        assertEquals(noteThird, get(noteThird.id))
    }

    @Test fun getByIdList() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(listOf(noteFirst, noteThird), get(listOf(
                noteFirst.id, noteThird.id, Random.nextLong()
        )))

        assertEquals(listOf(noteSecond.copy(isBin = false), noteThird), get(listOf(
                noteSecond.id, noteThird.id
        )))
    }

    @Test fun getByBin() = inNoteDao {
        insert(noteFirst)
        insert(noteSecond)
        insert(noteThird)

        assertEquals(listOf(noteFirst, noteThird), get(bin = false))
        assertEquals(listOf(noteSecond), get(bin = true))
    }


    @Test fun getByChange() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                noteThird, noteSecond.copy(isBin = false), noteFirst
        ), getByChange(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                noteThird.copy(isBin = true), noteSecond, noteFirst.copy(isBin = true)
        ), getByChange(bin = true))
    }

    @Test fun getByCreate() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                noteThird, noteSecond.copy(isBin = false), noteFirst
        ), getByCreate(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                noteThird.copy(isBin = true), noteSecond, noteFirst.copy(isBin = true)
        ), getByCreate(bin = true))
    }

    @Test fun getByRank() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                noteSecond.copy(isBin = false), noteFirst, noteThird
        ), getByRank(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                noteSecond, noteFirst.copy(isBin = true), noteThird.copy(isBin = true)
        ), getByRank(bin = true))
    }

    @Test fun getByColor() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(arrayListOf(
                noteSecond.copy(isBin = false), noteFirst, noteThird
        ), getByColor(bin = false))

        updateAllTo(bin = true)

        assertEquals(arrayListOf(
                noteSecond, noteFirst.copy(isBin = true), noteThird.copy(isBin = true)
        ), getByColor(bin = true))
    }


    private companion object {
        val noteFirst = NoteEntity(
                id = 1, create = DATE_1, change = DATE_2, text = "123", name = "456",
                color = 1, type = NoteType.TEXT, rankPs = 1
        )

        val noteSecond = NoteEntity(
                id = 2, create = DATE_2, change = DATE_3, text = "654", name = "321",
                color = 1, type = NoteType.TEXT, rankPs = 1, isBin = true
        )

        val noteThird = NoteEntity(
                id = 3, create = DATE_3, change = DATE_4, text = "123", name = "",
                color = 3, type = NoteType.TEXT, rankPs = 2
        )
    }

}