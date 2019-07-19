package sgtmelon.scriptum.test.integration.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.dao.NoteDao
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Интеграционный тест для [NoteDao]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentIntegrationTest() {

    private fun NoteDao.insertAllTo(bin: Boolean) {
        insert(noteFirst.copy(isBin = bin))
        insert(noteSecond.copy(isBin = bin))
        insert(noteThird.copy(isBin = bin))
    }

    @Test fun delete() = inTheRoom {
        with(getNoteDao()) {
            insert(noteFirst)
            assertEquals(noteFirst, get(noteFirst.id))

            delete(noteFirst)
            assertNull(get(noteFirst.id))
        }
    }

    @Test fun update() = inTheRoom {
        with(getNoteDao()) {
            insert(noteFirst)
            assertEquals(noteFirst, get(noteFirst.id))

            noteFirst.copy(color = 10, isBin = true).let {
                update(it)
                assertEquals(it, get(noteFirst.id))
            }
        }
    }

    @Test fun updateByList() = inTheRoom {
        with(getNoteDao()) {
            insert(noteFirst)
            insert(noteThird)
            assertEquals(arrayListOf(noteFirst, noteThird), getByColor(bin = false))

            noteFirst.copy(color = 3).let {first ->
                noteThird.copy(color = 2).let {third ->
                    update(arrayListOf(first, third))
                    assertEquals(arrayListOf(third, first), getByColor(bin = false))
                }
            }
        }
    }

    @Test fun getOnWrongId() = inTheRoom { assertNull(getNoteDao()[Random.nextLong()]) }

    @Test fun getOnCorrectId() = inTheRoom {
        with(getNoteDao()) {
            insert(noteFirst)
            insert(noteSecond)
            insert(noteThird)

            assertEquals(noteFirst, get(noteFirst.id))
            assertEquals(noteSecond, get(noteSecond.id))
            assertEquals(noteThird, get(noteThird.id))
        }
    }

    @Test fun getFromPage() = inTheRoom {
        with(getNoteDao()) {
            assertEquals(arrayListOf<NoteEntity>(), get(bin = false))
            assertEquals(arrayListOf<NoteEntity>(), get(bin = true))

            insert(noteFirst)
            insert(noteSecond)

            assertEquals(arrayListOf(noteFirst), get(bin = false))
            assertEquals(arrayListOf(noteSecond), get(bin = true))
        }
    }

    @Test fun getByChange() = inTheRoom {
        with(getNoteDao()) {
            insertAllTo(bin = false)

            assertEquals(arrayListOf(
                    noteThird, noteSecond.copy(isBin = false), noteFirst
            ), getByChange(bin = false))

            clearAllTables()

            insertAllTo(bin = true)

            assertEquals(arrayListOf(
                    noteThird.copy(isBin = true), noteSecond, noteFirst.copy(isBin = true)
            ), getByChange(bin = true))
        }
    }

    @Test fun getByCreate() = inTheRoom {
        with(getNoteDao()) {
            insertAllTo(bin = false)

            assertEquals(arrayListOf(
                    noteThird, noteSecond.copy(isBin = false), noteFirst
            ), getByCreate(bin = false))

            clearAllTables()

            insertAllTo(bin = true)

            assertEquals(arrayListOf(
                    noteThird.copy(isBin = true), noteSecond, noteFirst.copy(isBin = true)
            ), getByCreate(bin = true))
        }
    }

    @Test fun getByRank() = inTheRoom {
        with(getNoteDao()) {
            insertAllTo(bin = false)

            assertEquals(arrayListOf(
                    noteSecond.copy(isBin = false), noteFirst, noteThird
            ), getByRank(bin = false))

            clearAllTables()

            insertAllTo(bin = true)

            assertEquals(arrayListOf(
                    noteSecond, noteFirst.copy(isBin = true), noteThird.copy(isBin = true)
            ), getByRank(bin = true))
        }
    }

    @Test fun getByColor() = inTheRoom {
        with(getNoteDao()) {
            insertAllTo(bin = false)

            assertEquals(arrayListOf(
                    noteSecond.copy(isBin = false), noteFirst, noteThird
            ), getByColor(bin = false))

            clearAllTables()

            insertAllTo(bin = true)

            assertEquals(arrayListOf(
                    noteSecond, noteFirst.copy(isBin = true), noteThird.copy(isBin = true)
            ), getByColor(bin = true))
        }
    }

    @Test fun getCount() = inTheRoom {
        with(getNoteDao()) {
            insertAllTo(bin = false)

            assertEquals(0, getCount(arrayListOf(1, 2, 3), NoteType.ROLL.ordinal))
            assertEquals(1, getCount(arrayListOf(1, 4, 5), NoteType.TEXT.ordinal))
            assertEquals(2, getCount(arrayListOf(1, 2, 5), NoteType.TEXT.ordinal))
            assertEquals(3, getCount(arrayListOf(1, 2, 3), NoteType.TEXT.ordinal))
        }
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