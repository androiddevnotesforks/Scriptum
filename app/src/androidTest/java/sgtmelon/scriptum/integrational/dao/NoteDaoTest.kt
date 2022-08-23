package sgtmelon.scriptum.integrational.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.parent.ParentRoomTest

/**
 * Integration test for [NoteDao]
 */
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentRoomTest() {

    @Test fun todo() {
        TODO()
    }

    //    //region Variables
    //
    //    private val firstNote = NoteEntity(
    //        id = 1, create = DATE_1, change = DATE_2, text = "123", name = "456",
    //        color = Color.PURPLE, type = NoteType.TEXT, rankId = -1, rankPs = -1, isBin = false,
    //        isStatus = Random.nextBoolean()
    //    )
    //
    //    private val secondNote = NoteEntity(
    //        id = 2, create = DATE_2, change = DATE_3, text = "654", name = "321",
    //        color = Color.PURPLE, type = NoteType.TEXT, rankId = 1, rankPs = 1, isBin = true,
    //        isStatus = Random.nextBoolean()
    //    )
    //
    //    private val thirdNote = NoteEntity(
    //        id = 3, create = DATE_3, change = DATE_4, text = "123", name = "",
    //        color = Color.BLUE, type = NoteType.TEXT, rankId = 2, rankPs = 2, isBin = false,
    //        isStatus = Random.nextBoolean()
    //    )
    //
    //    private val fourthNote = NoteEntity(
    //        id = 4, create = DATE_4, change = DATE_5, text = "789", name = "",
    //        color = Color.BLUE, type = NoteType.TEXT, rankId = 2, rankPs = 2, isBin = false,
    //        isStatus = Random.nextBoolean()
    //    )
    //
    //    //endregion
    //
    //    private fun inNoteDao(func: suspend NoteDao.() -> Unit) = inRoomTest {
    //        noteDao.apply { func() }
    //    }
    //
    //    private suspend fun NoteDao.insertAllTo(isBin: Boolean) {
    //        insert(firstNote.copy(isBin = isBin))
    //        insert(secondNote.copy(isBin = isBin))
    //        insert(thirdNote.copy(isBin = isBin))
    //        insert(fourthNote.copy(isBin = isBin))
    //
    //        assertNotNull(get(firstNote.id))
    //        assertNotNull(get(secondNote.id))
    //        assertNotNull(get(thirdNote.id))
    //        assertNotNull(get(fourthNote.id))
    //    }
    //
    //    private suspend fun NoteDao.updateAllTo(isBin: Boolean) {
    //        update(firstNote.copy(isBin = isBin))
    //        update(secondNote.copy(isBin = isBin))
    //        update(thirdNote.copy(isBin = isBin))
    //        update(fourthNote.copy(isBin = isBin))
    //    }
    //
    //    // Dao common functions
    //
    //    @Test fun insertWithUnique() = inNoteDao {
    //        assertEquals(1, insert(firstNote))
    //        assertEquals(DaoConst.UNIQUE_ERROR_ID, insert(firstNote))
    //    }
    //
    //    @Test fun delete() = inNoteDao {
    //        insert(firstNote)
    //        assertEquals(firstNote, get(firstNote.id))
    //
    //        delete(firstNote)
    //        assertNull(get(firstNote.id))
    //    }
    //
    //    @Test fun deleteByList() = inNoteDao {
    //        insertAllTo(isBin = false)
    //
    //        val list = listOf(firstNote, secondNote, thirdNote, fourthNote)
    //
    //        delete(list)
    //        for (it in list) {
    //            assertNull(get(it.id))
    //        }
    //    }
    //
    //    @Test fun update() = inNoteDao {
    //        insert(firstNote)
    //        assertEquals(firstNote, get(firstNote.id))
    //
    //        firstNote.copy(color = Color.WHITE, isBin = true).let {
    //            update(it)
    //            assertEquals(it, get(firstNote.id))
    //        }
    //    }
    //
    //    @Test fun updateByList() = inNoteDao {
    //        insert(firstNote)
    //        insert(thirdNote)
    //        assertEquals(arrayListOf(firstNote, thirdNote), getByColor(bin = false))
    //
    //        firstNote.copy(color = Color.BLUE).let { first ->
    //            thirdNote.copy(color = Color.INDIGO).let { third ->
    //                update(arrayListOf(first, third))
    //                assertEquals(arrayListOf(third, first), getByColor(bin = false))
    //            }
    //        }
    //    }
    //
    //    // Dao get functions
    //
    //    @Test fun getCount() = inNoteDao {
    //        assertEquals(0, getCount(isBin = false, rankIdList = listOf()))
    //        assertEquals(0, getCount(isBin = true, rankIdList = listOf()))
    //
    //        insert(firstNote)
    //        insert(secondNote)
    //        insert(thirdNote)
    //
    //        assertEquals(1, getCount(isBin = false, rankIdList = listOf()))
    //        assertEquals(2, getCount(isBin = false, rankIdList = listOf(2)))
    //        assertEquals(1, getCount(isBin = true, rankIdList = listOf(1)))
    //    }
    //
    //    @Test fun getCountCrowd() = inNoteDao { getCount(Random.nextBoolean(), overflowLongList) }
    //
    //    @Test fun getBindCount() = inNoteDao {
    //        assertEquals(0, getBindCount(listOf()))
    //
    //        insert(firstNote)
    //        insert(secondNote)
    //        insert(thirdNote)
    //
    //        val bindCount = listOf(firstNote, secondNote, thirdNote).count { it.isStatus }
    //        assertEquals(bindCount, getBindCount(listOf(firstNote.id, secondNote.id, thirdNote.id)))
    //    }
    //
    //    @Test fun getBindCountCrowd() = inNoteDao { getBindCount(overflowLongList) }
    //
    //    @Test fun getByWrongId() = inNoteDao { assertNull(get(Random.nextLong())) }
    //
    //    @Test fun getByCorrectId() = inNoteDao {
    //        insert(firstNote)
    //        assertEquals(firstNote, get(firstNote.id))
    //
    //        insert(secondNote)
    //        assertEquals(secondNote, get(secondNote.id))
    //
    //        insert(thirdNote)
    //        assertEquals(thirdNote, get(thirdNote.id))
    //    }
    //
    //    @Test fun getByIdList() = inNoteDao {
    //        insertAllTo(isBin = false)
    //
    //        assertEquals(
    //            listOf(firstNote, thirdNote),
    //            get(listOf(firstNote.id, thirdNote.id, Random.nextLong()))
    //        )
    //
    //        assertEquals(
    //            listOf(secondNote.copy(isBin = false), thirdNote),
    //            get(listOf(secondNote.id, thirdNote.id))
    //        )
    //    }
    //
    //    @Test fun getByIdListCrowd() = inNoteDao { get(overflowLongList) }
    //
    //    @Test fun getByBin() = inNoteDao {
    //        insert(firstNote)
    //        insert(secondNote)
    //        insert(thirdNote)
    //
    //        assertEquals(listOf(firstNote, thirdNote), get(bin = false))
    //        assertEquals(listOf(secondNote), get(bin = true))
    //    }
    //
    //    // Dao get by different sorts functions
    //
    //    @Test fun getByChange() = inNoteDao {
    //        insertAllTo(isBin = false)
    //
    //        assertEquals(arrayListOf(
    //            fourthNote, thirdNote, secondNote.copy(isBin = false), firstNote
    //        ), getByChange(bin = false))
    //
    //        updateAllTo(isBin = true)
    //
    //        assertEquals(arrayListOf(
    //            fourthNote.copy(isBin = true), thirdNote.copy(isBin = true),
    //            secondNote, firstNote.copy(isBin = true)
    //        ), getByChange(bin = true))
    //    }
    //
    //    @Test fun getByCreate() = inNoteDao {
    //        insertAllTo(isBin = false)
    //
    //        assertEquals(arrayListOf(
    //            fourthNote, thirdNote, secondNote.copy(isBin = false), firstNote
    //        ), getByCreate(bin = false))
    //
    //        updateAllTo(isBin = true)
    //
    //        assertEquals(arrayListOf(
    //            fourthNote.copy(isBin = true), thirdNote.copy(isBin = true),
    //            secondNote, firstNote.copy(isBin = true)
    //        ), getByCreate(bin = true))
    //    }
    //
    //    @Test fun getByRank() = inNoteDao {
    //        insertAllTo(isBin = false)
    //
    //        assertEquals(arrayListOf(
    //            firstNote, secondNote.copy(isBin = false), fourthNote, thirdNote
    //        ), getByRank(bin = false))
    //
    //        updateAllTo(isBin = true)
    //
    //        assertEquals(arrayListOf(
    //            firstNote.copy(isBin = true),
    //            secondNote,
    //            fourthNote.copy(isBin = true),
    //            thirdNote.copy(isBin = true)
    //        ), getByRank(bin = true))
    //    }
    //
    //    @Test fun getByColor() = inNoteDao {
    //        insertAllTo(isBin = false)
    //
    //        assertEquals(arrayListOf(
    //            secondNote.copy(isBin = false), firstNote, thirdNote, fourthNote
    //        ), getByColor(bin = false))
    //
    //        updateAllTo(isBin = true)
    //
    //        assertEquals(arrayListOf(
    //            secondNote,
    //            firstNote.copy(isBin = true),
    //            thirdNote.copy(isBin = true),
    //            fourthNote.copy(isBin = true)
    //        ), getByColor(bin = true))
    //    }
}