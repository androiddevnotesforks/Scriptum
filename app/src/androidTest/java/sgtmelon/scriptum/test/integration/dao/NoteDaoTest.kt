package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.dao.NoteDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [NoteDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentIntegrationTest() {

    private fun inNoteDao(func: NoteDao.() -> Unit) = inRoom { iNoteDao.apply(func) }

    private fun NoteDao.insertAllTo(bin: Boolean) {
        insert(noteFirst.copy(isBin = bin))
        insert(noteSecond.copy(isBin = bin))
        insert(noteThird.copy(isBin = bin))
    }

    private fun NoteDao.updateAllTo(bin: Boolean) {
        update(noteFirst.copy(isBin = bin))
        update(noteSecond.copy(isBin = bin))
        update(noteThird.copy(isBin = bin))
    }

    @Test fun insertWithUnique() = inNoteDao {
        repeat(times = 2) { insert(noteFirst) }
        assertTrue(getByChange(bin = false).size == 1)
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

    @Test fun getOnWrongId() = inNoteDao { assertNull(get(Random.nextLong())) }

    @Test fun getOnCorrectId() = inNoteDao {
        insert(noteFirst)
        assertEquals(noteFirst, get(noteFirst.id))

        insert(noteSecond)
        assertEquals(noteSecond, get(noteSecond.id))

        insert(noteThird)
        assertEquals(noteThird, get(noteThird.id))
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

    @Test fun getCount() = inNoteDao {
        insertAllTo(bin = false)

        assertEquals(0, getCount(arrayListOf(1, 2, 3), NoteType.ROLL.ordinal))
        assertEquals(1, getCount(arrayListOf(1, 4, 5), NoteType.TEXT.ordinal))
        assertEquals(2, getCount(arrayListOf(1, 2, 5), NoteType.TEXT.ordinal))
        assertEquals(3, getCount(arrayListOf(1, 2, 3), NoteType.TEXT.ordinal))
    }

    private companion object {
        val noteFirst = NoteEntity(
                id = 1, create = DATE_1, change = DATE_2, text = "123", name = "456",
                color = 1, type = NoteType.TEXT, rankPs = arrayListOf(1, 3)
        )

        val noteSecond = NoteEntity(
                id = 2, create = DATE_2, change = DATE_3, text = "654", name = "321",
                color = 1, type = NoteType.TEXT, rankPs = arrayListOf(1, 3), isBin = true
        )

        val noteThird = NoteEntity(
                id = 3, create = DATE_3, change = DATE_4, text = "123", name = "",
                color = 3, type = NoteType.TEXT, rankPs = arrayListOf(2)
        )
    }

}