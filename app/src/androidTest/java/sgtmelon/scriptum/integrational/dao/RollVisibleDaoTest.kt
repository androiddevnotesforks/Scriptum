package sgtmelon.scriptum.integrational.dao

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.annotation.DaoConst
import sgtmelon.scriptum.infrastructure.database.dao.RollVisibleDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.scriptum.parent.provider.EntityProvider.nextNoteEntity
import sgtmelon.scriptum.parent.provider.EntityProvider.nextRollVisibleEntity

/**
 * Integration test for [RollVisibleDao] and safe functions.
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class RollVisibleDaoTest : ParentRoomTest() {

    //region Variables

    private val firstNote = nextNoteEntity(id = 1)
    private val secondNote = nextNoteEntity(id = 2)

    private val firstVisible = nextRollVisibleEntity(id = 1, firstNote.id)
    private val secondVisible = nextRollVisibleEntity(id = 2, secondNote.id)

    //endregion

    //region Help functions

    private suspend fun Database.insertRelation(note: NoteEntity, rollVisible: RollVisibleEntity) {
        noteDao.insertSafe(note)
        assertEquals(noteDao.get(note.id), note)
        rollVisibleDao.insertSafe(rollVisible)
        assertEquals(rollVisibleDao.getVisible(rollVisible.noteId), rollVisible.value)
    }

    private fun getListsPair(): Pair<List<NoteEntity>, List<RollVisibleEntity>> {
        val noteList = overflowDelegator.getList { nextNoteEntity((it + 1).toLong()) }
        val rollVisibleList = overflowDelegator.getList(noteList.size) {
            nextRollVisibleEntity((it + 1).toLong(), noteList[it].id)
        }

        assertEquals(noteList.size, rollVisibleList.size)

        return noteList to rollVisibleList
    }

    private fun insertBigData(): Pair<List<NoteEntity>, List<RollVisibleEntity>> {
        val pair = getListsPair()
        val (noteList, visibleList) = pair

        inRoomTest {
            for (i in noteList.indices) {
                insertRelation(noteList[i], visibleList[i])

                assertNotNull(noteDao.get(noteList[i].id))
                assertNotNull(rollVisibleDao.getVisible(visibleList[i].noteId))
            }
        }

        return pair
    }

    //endregion

    override fun setUp() {
        super.setUp()

        assertNotEquals(firstVisible.noteId, secondVisible.noteId)
    }

    @Test fun parentStrategy_onDelete() = inRoomTest {
        val note = firstNote
        val rollVisible = firstVisible

        insertRelation(note, rollVisible)
        assertEquals(rollVisibleDao.getVisible(note.id), rollVisible.value)
        noteDao.delete(note)
        assertNull(rollVisibleDao.getVisible(note.id))
    }

    @Test fun insert() = inRoomTest { insertRelation(firstNote, firstVisible) }

    /**
     * Check OnConflictStrategy.IGNORE on inserting with same [RollVisibleEntity.id].
     */
    @Test fun insertWithSameId() {
        inRoomTest {
            insertRelation(firstNote, firstVisible)

            val conflict = firstVisible.copy(value = !firstVisible.value)
            assertEquals(rollVisibleDao.insert(conflict), DaoConst.UNIQUE_ERROR_ID)

            assertEquals(rollVisibleDao.getVisible(firstVisible.noteId), firstVisible.value)
        }
    }

    /**
     * Check what only one [RollVisibleEntity] may be attached to one [NoteEntity].
     */
    @Test fun insertWithNoteIdUnique() {
        inRoomTest {
            insertRelation(firstNote, firstVisible)

            val unique = secondVisible.copy(noteId = firstNote.id)
            assertEquals(rollVisibleDao.insert(unique), DaoConst.UNIQUE_ERROR_ID)

            assertEquals(rollVisibleDao.getVisible(firstVisible.noteId), firstVisible.value)
        }
    }

    /**
     * If insert [RollVisibleEntity] not attached to [NoteEntity] you will receive error:
     * - android.database.sqlite.SQLiteConstraintException: FOREIGN KEY constraint failed (code 787)
     *
     * This test check this situation.
     */
    @Test fun insertSafe_throwsCheck() {
        inRoomTest {
            exceptionRule.expect(SQLiteConstraintException::class.java)
            rollVisibleDao.insert(firstVisible)
        }
    }

    @Test fun insertSafe() = inRoomTest {
        assertNull(rollVisibleDao.insertSafe(firstVisible))

        noteDao.insert(firstNote)
        assertEquals(rollVisibleDao.insertSafe(firstVisible), firstVisible.id)

        /** This check of OnConflictStrategy.IGNORE after first insert. */
        assertNull(rollVisibleDao.insertSafe(firstVisible))
    }

    @Test fun update() = inRoomTest {
        insertRelation(firstNote, firstVisible)

        val update = !firstVisible.value
        rollVisibleDao.update(firstNote.id, update)
        assertEquals(rollVisibleDao.getVisible(firstVisible.noteId), update)
    }

    @Test fun getList() = inRoomTest {
        val (_, visibleList) = insertBigData()
        assertEquals(rollVisibleDao.getList(), visibleList)
    }

    @Test fun getList_byId_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)
        rollVisibleDao.getList(overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getList_byId() = inRoomTest {
        val resultList = listOf(firstVisible, secondVisible)
        val idList = resultList.map { it.noteId }

        assertTrue(rollVisibleDao.getListSafe(idList).isEmpty())

        insertRelation(firstNote, firstVisible)
        insertRelation(secondNote, secondVisible)
        assertEquals(rollVisibleDao.getListSafe(idList), resultList)
    }

    @Test fun getListSafe() = inRoomTest {
        assertTrue(
            rollVisibleDao.getListSafe(overflowDelegator.getList { Random.nextLong() }).isEmpty()
        )

        val (noteList, visibleList) = insertBigData()

        assertEquals(rollVisibleDao.getListSafe(noteList.map { it.id }), visibleList)
    }

    @Test fun getVisible() = inRoomTest {
        assertNull(rollVisibleDao.getVisible(Random.nextLong()))

        insertRelation(firstNote, firstVisible)
        assertEquals(rollVisibleDao.getVisible(firstVisible.noteId), firstVisible.value)
    }
}