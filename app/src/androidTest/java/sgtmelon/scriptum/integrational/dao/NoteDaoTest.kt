package sgtmelon.scriptum.integrational.dao

import android.database.sqlite.SQLiteException
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.math.abs
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.NoteDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getBindCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getRankVisibleCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_3
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_4
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_5
import sgtmelon.scriptum.parent.provider.EntityProvider.nextNoteEntity
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Integration test for [NoteDao] and safe functions.
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class NoteDaoTest : ParentRoomTest() {

    //region Variables

    private val firstNote = NoteEntity(
        id = 1, create = DATE_1, change = DATE_2, text = "123", name = "456",
        color = Color.PURPLE, type = NoteType.TEXT, rankId = -1, rankPs = -1, isBin = false,
        isStatus = Random.nextBoolean()
    )
    private val secondNote = NoteEntity(
        id = 2, create = DATE_2, change = DATE_3, text = "654", name = "321",
        color = Color.PURPLE, type = NoteType.TEXT, rankId = 1, rankPs = 1, isBin = true,
        isStatus = Random.nextBoolean()
    )
    private val thirdNote = NoteEntity(
        id = 3, create = DATE_3, change = DATE_4, text = "123", name = "",
        color = Color.BLUE, type = NoteType.TEXT, rankId = 2, rankPs = 2, isBin = false,
        isStatus = Random.nextBoolean()
    )
    private val fourthNote = NoteEntity(
        id = 4, create = DATE_4, change = DATE_5, text = "789", name = "",
        color = Color.BLUE, type = NoteType.TEXT, rankId = 2, rankPs = 2, isBin = false,
        isStatus = Random.nextBoolean()
    )

    private val list = listOf(firstNote, secondNote, thirdNote, fourthNote)

    //endregion

    //region Help functions

    private suspend fun Database.insert(note: NoteEntity) {
        noteDao.insertSafe(note)
        assertEquals(noteDao.get(note.id), note)
    }

    private suspend fun Database.insertAll(list: List<NoteEntity>) {
        for (note in list) insert(note)
    }

    private fun insertBigData(): List<NoteEntity> {
        val list = overflowDelegator.getList { nextNoteEntity((it + 1).toLong()) }

        inRoomTest {
            for (note in list) {
                insert(note)
                assertNotNull(noteDao.get(note.id))
            }
        }

        return list
    }

    //endregion

    @Test fun insert() = inRoomTest { insert(firstNote) }

    /**
     * Check OnConflictStrategy.IGNORE on inserting with same [NoteEntity.id].
     */
    @Test fun insertWithSameId() {
        inRoomTest {
            insert(firstNote)

            val conflict = firstNote.copy(text = nextString(), name = nextString())
            assertEquals(noteDao.insert(conflict), DaoConst.UNIQUE_ERROR_ID)

            assertEquals(noteDao.get(firstNote.id), firstNote)
        }
    }

    @Test fun insertSafe() = inRoomTest {
        assertEquals(noteDao.insertSafe(firstNote), firstNote.id)
        assertNull(noteDao.insertSafe(secondNote.copy(id = firstNote.id)))
    }

    @Test fun delete() = inRoomTest {
        insert(firstNote)
        noteDao.delete(firstNote)
        assertNull(noteDao.get(firstNote.id))
    }

    @Test fun deleteList() = inRoomTest {
        insertAll(list)
        noteDao.delete(listOf(firstNote, thirdNote))

        assertNull(noteDao.get(firstNote.id))
        assertEquals(noteDao.get(secondNote.id), secondNote)
        assertNull(noteDao.get(thirdNote.id))
        assertEquals(noteDao.get(fourthNote.id), fourthNote)
    }

    @Test fun update() = inRoomTest {
        insert(firstNote)

        val update = firstNote.copy(name = nextString(), text = nextString())
        noteDao.update(update)
        assertEquals(noteDao.get(firstNote.id), update)
    }

    @Test fun updateList() = inRoomTest {
        insertAll(list)

        val updateList = list.mapIndexed { i, it ->
            if (i.isDivideEntirely()) it.copy(name = nextString(), text = nextString()) else it
        }

        noteDao.update(updateList)
        for (note in updateList) {
            assertEquals(noteDao.get(note.id), note)
        }
    }

    @Test fun getNoCategoryCount() = inRoomTest {
        val list = list.map { it.copy(rankId = -1, isBin = Random.nextBoolean()) }

        insertAll(list)

        assertEquals(noteDao.getNoCategoryCount(isBin = false), list.filter { !it.isBin }.size)
        assertEquals(noteDao.getNoCategoryCount(isBin = true), list.filter { it.isBin }.size)
    }

    @Test fun getRankVisibleCount_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)

        val isBin = Random.nextBoolean()
        noteDao.getRankVisibleCount(isBin, overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getRankVisibleCount() = inRoomTest {
        val list = list.map {
            it.copy(rankId = abs(Random.nextLong()), isBin = Random.nextBoolean())
        }

        insertAll(list)

        val rankIdList = list.map { it.rankId }

        val noteSize = list.filter { !it.isBin }.size
        assertEquals(noteDao.getRankVisibleCountSafe(isBin = false, rankIdList), noteSize)
        val binSize = list.filter { it.isBin }.size
        assertEquals(noteDao.getRankVisibleCountSafe(isBin = true, rankIdList), binSize)
    }

    @Test fun getRankVisibleCountSafe() = inRoomTest {
        val list = insertBigData()

        val listWithRank = list.filter { it.rankId != -1L }
        val rankIdList = list.asSequence()
            .map { it.rankId }
            .filter { it != -1L }
            .toList()

        assertEquals(listWithRank.size, rankIdList.size)

        val noteSize = listWithRank.filter { !it.isBin }.size
        assertEquals(noteDao.getRankVisibleCountSafe(isBin = false, rankIdList), noteSize)
        val binSize = listWithRank.filter { it.isBin }.size
        assertEquals(noteDao.getRankVisibleCountSafe(isBin = true, rankIdList), binSize)
    }

    @Test fun getBindCount_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)
        noteDao.getBindCount(overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getBindCount() = inRoomTest {
        val list = list.map { it.copy(isStatus = Random.nextBoolean()) }

        insertAll(list)

        assertEquals(noteDao.getBindCount(list.map { it.id }), list.filter { it.isStatus }.size)
    }

    @Test fun getBindCountSafe() = inRoomTest {
        val list = insertBigData()

        val idList = list.map { it.id }
        val statusSize = list.filter { it.isStatus }.size

        assertEquals(noteDao.getBindCountSafe(idList), statusSize)
    }

    @Test fun get_byWrongId() = inRoomTest { assertNull(noteDao.get(Random.nextLong())) }

    @Test fun get() = inRoomTest {
        val note = list.random()
        insert(note)
        assertEquals(noteDao.get(note.id), note)
    }

    @Test fun getList_byIdList_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)
        noteDao.getList(overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getList_byIdList() = inRoomTest {
        val list = list

        insertAll(list)

        val resultList = list.filterIndexed { i, _ -> i.isDivideEntirely() }
        assertEquals(noteDao.getList(resultList.map { it.id }), resultList)
    }

    @Test fun getListSafe() = inRoomTest {
        val list = insertBigData()

        val resultList = list.filterIndexed { i, _ -> i.isDivideEntirely() }
        assertEquals(noteDao.getListSafe(resultList.map { it.id }), resultList)
    }

    @Test fun getList_byBin() = inRoomTest {
        val list = list.map { it.copy(isBin = Random.nextBoolean()) }

        insertAll(list)

        val noteList = list.filter { !it.isBin }
        assertEquals(noteDao.getList(isBin = false), noteList)
        val binList = list.filter { it.isBin }
        assertEquals(noteDao.getList(isBin = true), binList)
    }

    @Test fun getListByChange() = inRoomTest {
        val isBin = Random.nextBoolean()
        val list = list.map { it.copy(isBin = isBin) }

        insertAll(list)

        assertEquals(noteDao.getListByChange(isBin), listOf(list[3], list[2], list[1], list[0]))
    }

    @Test fun getListByCreate() = inRoomTest {
        val isBin = Random.nextBoolean()
        val list = list.map { it.copy(isBin = isBin) }

        insertAll(list)

        assertEquals(noteDao.getListByCreate(isBin), listOf(list[3], list[2], list[1], list[0]))
    }

    @Test fun getListByRank() = inRoomTest {
        val isBin = Random.nextBoolean()
        val list = list.map { it.copy(isBin = isBin) }

        insertAll(list)

        assertEquals(noteDao.getListByRank(isBin), listOf(list[0], list[1], list[3], list[2]))
    }

    @Test fun getListByColor() = inRoomTest {
        val isBin = Random.nextBoolean()
        val list = list.map { it.copy(isBin = isBin) }

        insertAll(list)

        assertEquals(noteDao.getListByColor(isBin), listOf(list[1], list[0], list[3], list[2]))
    }
}