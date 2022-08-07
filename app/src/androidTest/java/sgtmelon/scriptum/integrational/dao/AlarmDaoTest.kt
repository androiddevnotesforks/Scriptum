package sgtmelon.scriptum.integrational.dao

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.room.RoomDb
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.extension.inRoomTest
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.database.dao.AlarmDao
import sgtmelon.scriptum.infrastructure.database.dao.safe.getListSafe
import sgtmelon.scriptum.infrastructure.database.dao.safe.insertSafe
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.parent.ParentRoomTest
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_1
import sgtmelon.scriptum.parent.provider.DateProvider.DATE_2
import sgtmelon.scriptum.parent.provider.DateProvider.nextDate


/**
 * Integration test for [AlarmDao].
 */
@RunWith(AndroidJUnit4::class)
class AlarmDaoTest : ParentRoomTest() {

    //region Variables

    private val firstNote = NoteEntity(
        id = 1, create = DATE_1, change = DATE_1, text = nextString(), name = nextString(),
        color = Color.values().random(), type = NoteType.values().random()
    )

    private val secondNote = NoteEntity(
        id = 2, create = DATE_2, change = DATE_2, text = nextString(), name = nextString(),
        color = Color.values().random(), type = NoteType.values().random()
    )

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

    private val notificationList = arrayListOf(firstNotification, secondNotification)

    //endregion

    private suspend fun RoomDb.insertAlarmRelation(note: NoteEntity, alarm: AlarmEntity) {
        noteDao.insert(note)
        assertEquals(noteDao.get(note.id), note)
        alarmDao.insert(alarm)
        assertEquals(alarmDao.get(alarm.noteId), alarm)
    }

    // Dao tests

    @Test fun insert() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.get(firstNote.id), firstAlarm)
    }

    /**
     * Check OnConflictStrategy.REPLACE on inserting with same [AlarmEntity.id].
     */
    @Test fun insertWithConflict_replace() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val conflictAlarm = secondAlarm.copy(id = firstAlarm.id, noteId = firstAlarm.noteId)
        assertEquals(alarmDao.insert(conflictAlarm), firstAlarm.id)

        assertEquals(alarmDao.get(firstNote.id), conflictAlarm)
    }

    /**
     * Check what only one [AlarmEntity] may be attached to one [NoteEntity].
     */
    @Test fun insertWithNoteIdUnique() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val uniqueAlarm = secondAlarm.copy(noteId = firstNote.id)
        assertEquals(alarmDao.insert(uniqueAlarm), uniqueAlarm.id)

        assertEquals(alarmDao.get(firstNote.id), uniqueAlarm)
    }

    /**
     * If insert [AlarmEntity] not attached to [NoteEntity] you will receive error:
     * - android.database.sqlite.SQLiteConstraintException: FOREIGN KEY constraint failed (code 787)
     *
     * This test check
     */
    @Test fun insertSafe_throwsCheck() = inRoomTest {
        exceptionRule.expect(SQLiteConstraintException::class.java)
        alarmDao.insert(firstAlarm)
    }

    @Test fun insertSafe() = inRoomTest {
        assertNull(alarmDao.insertSafe(firstAlarm))

        noteDao.insert(firstNote)
        assertEquals(alarmDao.insertSafe(firstAlarm), firstAlarm.id)
    }

    @Test fun delete() = inRoomTest {
        assertNull(alarmDao.get(firstAlarm.noteId))
        alarmDao.delete(firstAlarm.noteId)

        insertAlarmRelation(firstNote, firstAlarm)

        alarmDao.delete(firstAlarm.noteId)
        assertNull(alarmDao.get(firstAlarm.noteId))
    }

    @Test fun update() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)

        val updateAlarm = firstAlarm.copy(date = DATE_2)
        alarmDao.update(updateAlarm)
        assertEquals(alarmDao.get(firstAlarm.noteId), updateAlarm)
    }

    @Test fun get_withWrongId() = inRoomTest { assertNull(alarmDao.get(Random.nextLong())) }

    @Test fun get_withCorrectId() = inRoomTest {
        insertAlarmRelation(firstNote, firstAlarm)
        assertEquals(alarmDao.get(firstAlarm.noteId), firstAlarm)
    }

    @Test fun getList() = inRoomTest {
        insertAlarmRelation(secondNote, secondAlarm)
        insertAlarmRelation(firstNote, firstAlarm)

        assertEquals(alarmDao.getList(), listOf(firstAlarm, secondAlarm))
    }

    @Test fun getList_byId_overflowCheck() = inRoomTest {
        exceptionRule.expect(SQLiteException::class.java)
        alarmDao.getList(overflowDelegator.getList { Random.nextLong() })
    }

    @Test fun getListSafe() = inRoomTest {
        assertTrue(alarmDao.getListSafe(overflowDelegator.getList { Random.nextLong() }).isEmpty())

        val noteList = overflowDelegator.getList {
            NoteEntity(
                id = (it + 1).toLong(), create = nextDate(), change = nextDate(),
                text = nextString(), name = nextString(),
                color = Color.values().random(), type = NoteType.values().random()
            )
        }
        val alarmList = overflowDelegator.getList(noteList.size) {
            AlarmEntity(id = (it + 1).toLong(), noteId = noteList[it].id, date = nextDate())
        }

        for (i in noteList.indices) {
            insertAlarmRelation(noteList[i], alarmList[i])
        }

        assertEquals(alarmDao.getListSafe(noteList.map { it.id }).size, noteList.size)
    }

    //region clean up


    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //    @Test fun getItemList() = inRoomTest {
    //        assertTrue(alarmDao.getItemList().isEmpty())
    //
    //        insertAlarmRelation(firstNote, firstAlarm)
    //        insertAlarmRelation(secondNote, secondAlarm)
    //
    //        assertEquals(notificationList, alarmDao.getItemList())
    //    }
    //
    //    @Test fun getDateList() {
    //        TODO()
    //    }
    //
    //    @Test fun getCount() = inRoomTest {
    //        var size = 0
    //
    //        assertEquals(size, alarmDao.getCount())
    //
    //        insertAlarmRelation(firstNote, firstAlarm)
    //        assertEquals(++size, alarmDao.getCount())
    //
    //        insertAlarmRelation(secondNote, secondAlarm)
    //        assertEquals(++size, alarmDao.getCount())
    //    }
    //
    //    @Test fun getCountByIdList() = inRoomTest {
    //        var size = 0
    //
    //        assertEquals(size, alarmDao.getCount(listOf()))
    //
    //        insertAlarmRelation(firstNote, firstAlarm)
    //        assertEquals(++size, alarmDao.getCount(listOf(firstNote.id)))
    //
    //        insertAlarmRelation(secondNote, secondAlarm)
    //        assertEquals(++size, alarmDao.getCount(listOf(firstNote.id, secondNote.id)))
    //    }
    //
    //    @Test fun getCountByIdListCrowd() = inRoomTest { alarmDao.getCount(crowdLongList) }

    //endregion
}