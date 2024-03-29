package sgtmelon.scriptum.tests.integration.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.dao.RollDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.deleteSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.infrastructure.database.model.DaoConst
import sgtmelon.scriptum.source.ParentRoomTest
import sgtmelon.scriptum.source.provider.EntityProvider.nextNoteEntity
import sgtmelon.scriptum.source.provider.EntityProvider.nextRollEntity
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString
import kotlin.math.abs
import kotlin.random.Random

/**
 * Integration test for [RollDao] and safe functions.
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class RollDaoTest : ParentRoomTest() {

    //region Variables

    private val firstSize = nextSize()
    private val secondSize = nextSize()

    private val firstPair: Pair<NoteEntity, List<RollEntity>>
        get() {
            val note = nextNoteEntity(id = 1)
            val list = List(firstSize) { nextRollEntity(it.toLong(), noteId = 1, it) }
            return note to list
        }

    private val secondPair: Pair<NoteEntity, List<RollEntity>>
        get() {
            val note = nextNoteEntity(id = 2)
            val list = List(secondSize) {
                nextRollEntity(id = firstSize + it.toLong(), noteId = 2, it)
            }

            return note to list
        }

    //endregion

    //region Help functions

    private fun nextSize() = (20..100).random()
    private fun nextSmallSize() = (2..5).random()

    private suspend fun Database.insertRelation(note: NoteEntity, rollList: List<RollEntity>) {
        noteDao.insertSafe(note)
        assertEquals(noteDao.get(note.id), note)

        for (it in rollList) {
            rollDao.insertSafe(it)
        }
        assertEquals(rollDao.getList(note.id), rollList)
    }

    private fun getListsPair(): Pair<List<NoteEntity>, List<List<RollEntity>>> {
        val noteList = overflowDelegator.getList { nextNoteEntity((it + 1).toLong()) }

        /** It needed for correct inserting of [RollEntity] inside [RollDao] */
        var rollIdShift = 0
        val rollsList = overflowDelegator.getList(noteList.size) { itRoll ->
            val id = itRoll + 1L
            val noteId = noteList[itRoll].id

            return@getList List(nextSmallSize()) {
                nextRollEntity(
                    id = rollIdShift++ + id,
                    noteId,
                    it
                )
            }
        }

        assertEquals(noteList.size, rollsList.size)

        return noteList to rollsList
    }

    private fun insertBigData(): Pair<List<NoteEntity>, List<List<RollEntity>>> {
        val pair = getListsPair()
        val (noteList, rollsList) = pair

        inRoomTest {
            for (i in noteList.indices) {
                insertRelation(noteList[i], rollsList[i])

                assertNotNull(noteDao.get(noteList[i].id))
            }
        }

        return pair
    }

    private fun insertBigPair(): Pair<NoteEntity, List<RollEntity>> {
        val note = nextNoteEntity(id = 1)
        val rollList = overflowDelegator.getList { nextRollEntity((it + 1).toLong(), note.id, it) }

        inRoomTest { insertRelation(note, rollList) }

        return note to rollList
    }

    //endregion

    override fun setUp() {
        super.setUp()

        assertNotEquals(firstPair.first.id, secondPair.first.id)
    }

    @Test fun parentStrategy_onDelete() = inRoomTest {
        val (note, rollList) = firstPair

        insertRelation(note, rollList)
        assertEquals(rollDao.getList(note.id), rollList)
        noteDao.delete(note)
        assertTrue(rollDao.getList(note.id).isEmpty())
    }

    @Test fun insert() = inRoomTest { with(firstPair) { insertRelation(first, second) } }

    /**
     * Check OnConflictStrategy.IGNORE on inserting with same [RollEntity.id].
     */
    @Test fun insertWithSameId() {
        inRoomTest {
            val (note, rollList) = firstPair
            insertRelation(note, rollList)

            val roll = rollList.random()
            val conflict = roll.copy(isCheck = !roll.isCheck)
            assertEquals(rollDao.insert(conflict), DaoConst.UNIQUE_ERROR_ID)

            assertEquals(rollDao.getList(note.id).first { it.id == roll.id }, roll)
        }
    }

    /**
     * If insert [RollEntity] not attached to [NoteEntity] you will receive error:
     * - android.database.sqlite.SQLiteConstraintException: FOREIGN KEY constraint failed (code 787)
     *
     * This test check this situation.
     */
    @Test fun insertSafe_throwsCheck() {
        assertThrows(SQLiteConstraintException::class.java) {
            inRoomTest {
                rollDao.insert(firstPair.second.random())
            }
        }
    }

    @Test fun insertSafe() = inRoomTest {
        val (note, rollList) = firstPair
        val roll = rollList.random()

        assertNull(rollDao.insertSafe(roll))

        noteDao.insert(note)
        assertEquals(rollDao.insertSafe(roll), roll.id)

        /** This check of OnConflictStrategy.IGNORE after first insert. */
        assertNull(rollDao.insertSafe(roll))
    }

    @Test fun update() = inRoomTest {
        val (note, rollList) = firstPair
        insertRelation(note, rollList)

        val roll = rollList.random()
        val update = roll.copy(position = abs(Random.nextInt()), text = nextString())

        rollDao.update(update.id!!, update.position, update.text)

        assertEquals(rollDao.getList(note.id).first { it.id == update.id }, update)
    }

    @Test fun updateCheck() = inRoomTest {
        val (note, rollList) = firstPair
        insertRelation(note, rollList)

        val roll = rollList.random()
        val update = roll.copy(isCheck = !roll.isCheck)

        rollDao.updateCheck(update.id!!, update.isCheck)

        assertEquals(rollDao.getList(note.id).first { it.id == update.id }, update)
    }

    @Test fun delete() = inRoomTest {
        val (note, rollList) = firstPair
        insertRelation(note, rollList)

        assertEquals(rollDao.getList(note.id), rollList)
        rollDao.delete(note.id)
        assertTrue(rollDao.getList(note.id).isEmpty())
    }

    @Test fun delete_withExclude_overflowCheck() {
        overflowDelegator.expectException {
            inRoomTest {
                val (note, rollList) = firstPair
                insertRelation(note, rollList)
                rollDao.delete(note.id, it.getList { Random.nextLong() })
            }
        }
    }

    @Test fun delete_withExclude() = inRoomTest {
        val (note, rollList) = firstPair
        insertRelation(note, rollList)

        val idList = rollList.map { it.id!! }
        val excludeList = idList.filterNot { it.isDivideEntirely() }

        assertEquals(rollDao.getIdList(note.id), idList)
        rollDao.delete(note.id, excludeList)
        assertEquals(rollDao.getIdList(note.id), excludeList)
    }

    @Test fun delete_byIdList_overflowCheck() {
        overflowDelegator.expectException {
            inRoomTest {
                rollDao.delete(it.getList { Random.nextLong() })
            }
        }
    }

    @Test fun delete_byIdList() = inRoomTest {
        val (note, rollList) = firstPair
        insertRelation(note, rollList)

        val idList = rollList.map { it.id!! }
        val includeList = idList.filterNot { it.isDivideEntirely() }
        val excludeList = idList.toMutableList()
        excludeList.removeAll(includeList)

        assertEquals(rollDao.getIdList(note.id), idList)
        rollDao.delete(includeList)
        assertEquals(rollDao.getIdList(note.id), excludeList)
    }

    @Test fun deleteSafe_withSmallExclude() = inRoomTest {
        val (note, rollList) = insertBigPair()

        val excludeList = rollList.take((1..DaoConst.OVERFLOW_COUNT).random()).map { it.id!! }

        assertTrue(excludeList.size <= DaoConst.OVERFLOW_COUNT)

        rollDao.deleteSafe(note.id, excludeList)
        assertEquals(rollDao.getIdList(note.id), excludeList)
    }

    @Test fun deleteSafe() = inRoomTest {
        val (note, rollList) = insertBigPair()

        val excludeList = rollList.asSequence()
            .map { it.id!! }
            .filter { it.isDivideEntirely() }
            .toList()

        assertTrue(excludeList.size > DaoConst.OVERFLOW_COUNT)

        rollDao.deleteSafe(note.id, excludeList)
        assertEquals(rollDao.getIdList(note.id), excludeList)
    }

    @Test fun getList() = inRoomTest {
        val (firstNote, firstRollList) = firstPair
        val (secondNote, secondRollList) = secondPair

        insertRelation(firstNote, firstRollList)
        assertEquals(rollDao.getList(), firstRollList)

        insertRelation(secondNote, secondRollList)
        val resultList = mutableListOf<RollEntity>()
        resultList.addAll(firstRollList)
        resultList.addAll(secondRollList)
        assertEquals(rollDao.getList(), resultList)
    }

    @Test fun getIdList() = inRoomTest {
        val (firstNote, firstRollList) = firstPair
        val (secondNote, secondRollList) = secondPair

        insertRelation(firstNote, firstRollList)
        insertRelation(secondNote, secondRollList)

        assertEquals(rollDao.getIdList(firstNote.id), firstRollList.map { it.id })
        assertEquals(rollDao.getIdList(secondNote.id), secondRollList.map { it.id })
    }

    @Test fun getList_byNoteIdList_overflowCheck() {
        overflowDelegator.expectException {
            inRoomTest {
                rollDao.getList(it.getList { Random.nextLong() })
            }
        }
    }

    @Test fun getList_byNoteIdList() = inRoomTest {
        val (firstNote, firstRollList) = firstPair
        val (secondNote, secondRollList) = secondPair

        insertRelation(firstNote, firstRollList)
        insertRelation(secondNote, secondRollList)

        val resultList = mutableListOf<RollEntity>()
        resultList.addAll(firstRollList)
        resultList.addAll(secondRollList)

        assertEquals(rollDao.getList(listOf(firstNote.id)), firstRollList)
        assertEquals(rollDao.getList(listOf(firstNote.id, secondNote.id)), resultList)
        assertTrue(rollDao.getList(listOf(abs(Random.nextLong()) + 2)).isEmpty())
    }

    @Test fun getListSafe() = inRoomTest {
        val (noteList, rollsList) = insertBigData()

        val idList = noteList.asSequence()
            .filterIndexed { i, _ -> i.isDivideEntirely() }
            .map { it.id }
            .toList()

        val resultList = rollsList.filterIndexed { i, _ -> i.isDivideEntirely() }.flatten()

        assertEquals(rollDao.getListSafe(idList), resultList)
    }
}