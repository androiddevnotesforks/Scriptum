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
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.database.Database
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getCountSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2
import sgtmelon.scriptum.parent.provider.EntityProvider.nextAlarmEntity
import sgtmelon.scriptum.parent.provider.EntityProvider.nextNoteEntity
import sgtmelon.test.common.isDivideEntirely


/**
 * Integration test for [AlarmDao] and safe functions.
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentRoomTest() {

    //region Variables

    private val firstNote = nextNoteEntity(id = 1, DATE_1, DATE_1)
    private val secondNote = nextNoteEntity(id = 2, DATE_2, DATE_2)

    private val firstAlarm = AlarmEntity(id = 1, noteId = firstNote.id, date = DATE_1)
    private val secondAlarm = AlarmEntity(id = 2, noteId = secondNote.id, date = DATE_2)

    private val firstNotification = NotificationItem(
        with(firstNote) { NotificationItem.Note(id, name, color, type) },
        with(firstAlarm) { NotificationItem.Alarm(id, date) }
    )

    private val secondNotification = NotificationItem(
        with(secondNote) { NotificationItem.Note(id, name, color, type) },
        with(secondAlarm) { NotificationItem.Alarm(id, date) }
    )

    //endregion

    //region Help functions

    private suspend fun Database.insertRelation(note: NoteEntity, alarm: AlarmEntity) {
        noteDao.insert(note)
        assertEquals(noteDao.get(note.id), note)
        alarmDao.insert(alarm)
        assertEquals(alarmDao.get(alarm.noteId), alarm)
    }

    private fun getListsPair(): Pair<List<NoteEntity>, List<AlarmEntity>> {
        val noteList = overflowDelegator.getList { nextNoteEntity((it + 1).toLong()) }
        val alarmList = overflowDelegator.getList(noteList.size) {
            nextAlarmEntity((it + 1).toLong(), noteList[it].id)
        }

        assertEquals(noteList.size, alarmList.size)

        return noteList to alarmList
    }

    private fun insertBigData(): Pair<List<NoteEntity>, List<AlarmEntity>> {
        val pair = getListsPair()
        val (noteList, alarmList) = pair

        inRoomTest {
            for (i in noteList.indices) {
                insertRelation(noteList[i], alarmList[i])

                assertNotNull(noteDao.get(noteList[i].id))
                assertNotNull(alarmDao.get(alarmList[i].noteId))
            }
        }

        return pair
    }

    //endregion

    override fun setUp() {
        super.setUp()

        assertNotEquals(firstAlarm.noteId, secondAlarm.noteId)
    }

    @Test fun parentStrategy_onDelete() = inRoomTest {
        val note = firstNote
        val alarm = firstAlarm

        insertRelation(note, alarm)
        assertEquals(alarmDao.get(note.id), alarm)
        noteDao.delete(note)
        assertNull(alarmDao.get(note.id))
    }

    @Test fun insert() = inRoomTest { insertRelation(firstNote, firstAlarm) }

    /**
     * Check OnConflictStrategy.REPLACE on inserting with same [AlarmEntity.id].
     */
    @Test fun insertWithConflict_replace() = inRoomTest {
        insertRelation(firstNote, firstAlarm)

        val conflict = secondAlarm.copy(id = firstAlarm.id, noteId = firstAlarm.noteId)
        assertEquals(alarmDao.insert(conflict), firstAlarm.id)

        assertEquals(alarmDao.get(firstAlarm.noteId), conflict)
    }

    /**
     * Check what only one [AlarmEntity] may be attached to one [NoteEntity].
     */
    @Test fun insertWithNoteIdUnique() = inRoomTest {
        insertRelation(firstNote, firstAlarm)

        val unique = secondAlarm.copy(noteId = firstNote.id)
        assertEquals(alarmDao.insert(unique), unique.id)

        assertEquals(alarmDao.get(firstAlarm.noteId), unique)
    }

    /**
     * If insert [AlarmEntity] not attached to [NoteEntity] you will receive error:
     * - android.database.sqlite.SQLiteConstraintException: FOREIGN KEY constraint failed (code 787)
     *
     * This test check this situation.
     */
    @Test fun insertSafe_throwsCheck() = inRoomTest {
        exceptionRule.expect(SQLiteConstraintException::class.java)
        alarmDao.insert(firstAlarm)
    }

    @Test fun insertSafe() = inRoomTest {
        assertNull(alarmDao.insertSafe(firstAlarm))

        noteDao.insert(firstNote)
        assertEquals(alarmDao.insertSafe(firstAlarm), firstAlarm.id)

        /** This check of OnConflictStrategy.REPLACE after first insert. */
        assertEquals(alarmDao.insertSafe(firstAlarm), firstAlarm.id)
    }

    @Test fun delete() = inRoomTest {
        assertNull(alarmDao.get(firstAlarm.noteId))
        alarmDao.delete(firstAlarm.noteId)

        insertRelation(firstNote, firstAlarm)

        alarmDao.delete(firstAlarm.noteId)
        assertNull(alarmDao.get(firstAlarm.noteId))
    }

    @Test fun update() = inRoomTest {
        insertRelation(firstNote, firstAlarm)

        val update = firstAlarm.copy(date = DATE_2)
        alarmDao.update(update)
        assertEquals(alarmDao.get(firstAlarm.noteId), update)
    }

    @Test fun get_withWrongId() = inRoomTest { assertNull(alarmDao.get(Random.nextLong())) }

    @Test fun get_withCorrectId() = inRoomTest {
        insertRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.get(firstAlarm.noteId), firstAlarm)
    }

    @Test fun getList() = inRoomTest {
        insertRelation(secondNote, secondAlarm)
        insertRelation(firstNote, firstAlarm)

        assertEquals(alarmDao.getList(), listOf(firstAlarm, secondAlarm))
    }

    @Test fun getList_byId_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)
        alarmDao.getList(overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getList_byId() = inRoomTest {
        val resultList = listOf(firstAlarm, secondAlarm)
        val idList = resultList.map { it.noteId }

        assertTrue(alarmDao.getListSafe(idList).isEmpty())

        insertRelation(firstNote, firstAlarm)
        insertRelation(secondNote, secondAlarm)
        assertEquals(alarmDao.getListSafe(idList), resultList)
    }

    @Test fun getListSafe() = inRoomTest {
        assertTrue(alarmDao.getListSafe(overflowDelegator.getList { Random.nextLong() }).isEmpty())

        val (noteList, _) = insertBigData()

        assertEquals(alarmDao.getListSafe(noteList.map { it.id }).size, noteList.size)
    }

    @Test fun getItem() = inRoomTest {
        assertNull(alarmDao.getItem(Random.nextLong()))

        insertRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.getItem(firstAlarm.noteId), firstNotification)

        insertRelation(secondNote, secondAlarm)
        assertEquals(alarmDao.getItem(secondAlarm.noteId), secondNotification)
    }

    @Test fun getItemList() = inRoomTest {
        assertTrue(alarmDao.getItemList().isEmpty())

        insertRelation(firstNote, firstAlarm)
        insertRelation(secondNote, secondAlarm)
        assertEquals(alarmDao.getItemList(), listOf(firstNotification, secondNotification))
    }

    @Test fun getDateList() = inRoomTest {
        assertTrue(alarmDao.getDateList().isEmpty())

        insertRelation(firstNote, firstAlarm)
        insertRelation(secondNote, secondAlarm)

        val dateList = listOf(firstNotification, secondNotification).map { it.alarm.date }
        assertEquals(alarmDao.getDateList(), dateList)
    }

    @Test fun getCount() = inRoomTest {
        assertEquals(alarmDao.getCount(), 0)

        insertRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.getCount(), 1)

        insertRelation(secondNote, secondAlarm)
        assertEquals(alarmDao.getCount(), 2)
    }

    @Test fun getCount_withBigData() = inRoomTest {
        val (noteList, _) = insertBigData()
        assertEquals(alarmDao.getCount(), noteList.size)
    }

    @Test fun getCount_byIdList_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)
        alarmDao.getCount(overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getCount_byIdList() = inRoomTest {
        val maxSize = (10..100).random()

        val (noteList, alarmList) = getListsPair()
        for (i in noteList.indices) {
            if (i >= maxSize) break
            insertRelation(noteList[i], alarmList[i])
        }

        val filteredIdList = noteList.asSequence()
            .take(maxSize)
            .map { it.id }
            .filterNot { it.isDivideEntirely() }
            .toList()

        assertEquals(alarmDao.getCountSafe(filteredIdList), filteredIdList.size)
    }

    @Test fun getCountSafe() = inRoomTest {
        assertEquals(alarmDao.getCountSafe(overflowDelegator.getList { Random.nextLong() }), 0)

        val (noteList, _) = insertBigData()

        val filteredIdList = noteList.asSequence()
            .map { it.id }
            .filterNot { it.isDivideEntirely() }
            .toList()

        assertEquals(alarmDao.getCountSafe(filteredIdList), filteredIdList.size)
    }
}