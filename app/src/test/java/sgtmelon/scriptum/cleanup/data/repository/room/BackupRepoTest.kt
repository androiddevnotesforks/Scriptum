package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getRandomFutureTime
import sgtmelon.common.utils.getRandomPastTime
import sgtmelon.common.utils.getText
import sgtmelon.common.utils.nextShortString
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.cleanup.isDivideTwoEntirely
import sgtmelon.scriptum.cleanup.parent.ParentRoomRepoTest
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Test for [BackupRepo].
 */
@ExperimentalCoroutinesApi
class BackupRepoTest : ParentRoomRepoTest() {

    private val backupRepo by lazy { BackupRepo(roomProvider) }
    private val spyBackupRepo by lazy { spyk(backupRepo) }

    @Test fun insertData() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val noteList = mockk<MutableList<NoteEntity>>()
        val size = Random.nextInt()
        val removeList = mockk<List<NoteEntity>>()

        every { model.noteList } returns noteList
        every { noteList.size } returns size

        coEvery { spyBackupRepo.getRemoveNoteList(model, roomDb) } returns removeList
        coEvery { spyBackupRepo.clearList(removeList, model) } returns Unit
        coEvery { spyBackupRepo.clearRankList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.clearAlarmList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertNoteList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertRollList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertRollVisibleList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertRankList(model, roomDb) } returns Unit
        coEvery { spyBackupRepo.insertAlarmList(model, roomDb) } returns Unit

        assertEquals(ImportResult.Simple, spyBackupRepo.insertData(model, isSkipImports = false))

        val skipResult = ImportResult.Skip(skipCount = 0)
        assertEquals(skipResult, spyBackupRepo.insertData(model, isSkipImports = true))

        coVerifyOrder {
            spyBackupRepo.insertData(model, isSkipImports = false)
            roomProvider.openRoom()
            model.noteList
            noteList.size
            spyBackupRepo.clearRankList(model, roomDb)
            spyBackupRepo.clearAlarmList(model, roomDb)
            spyBackupRepo.insertNoteList(model, roomDb)
            spyBackupRepo.insertRollList(model, roomDb)
            spyBackupRepo.insertRollVisibleList(model, roomDb)
            spyBackupRepo.insertRankList(model, roomDb)
            spyBackupRepo.insertAlarmList(model, roomDb)

            spyBackupRepo.insertData(model, isSkipImports = true)
            roomProvider.openRoom()
            model.noteList
            noteList.size
            spyBackupRepo.getRemoveNoteList(model, roomDb)
            spyBackupRepo.clearList(removeList, model)
            spyBackupRepo.clearRankList(model, roomDb)
            spyBackupRepo.clearAlarmList(model, roomDb)
            spyBackupRepo.insertNoteList(model, roomDb)
            spyBackupRepo.insertRollList(model, roomDb)
            spyBackupRepo.insertRollVisibleList(model, roomDb)
            spyBackupRepo.insertRankList(model, roomDb)
            spyBackupRepo.insertAlarmList(model, roomDb)
            model.noteList
            noteList.size
        }
    }

    @Test fun getRemoveNoteList() {
        TODO()
    }

    @Test fun needSkipTextNote() {
        val item = mockk<NoteEntity>()
        val name = nextShortString()
        val text = nextString()

        val list = MutableList(size = 5) {
            NoteEntity(name = nextShortString(), text = nextString())
        }

        every { item.name } returns name
        every { item.text } returns text

        assertFalse(backupRepo.needSkipTextNote(item, list))

        list.add(item)

        assertTrue(backupRepo.needSkipTextNote(item, list))
    }

    @Test fun needSkipRollNote() {
        TODO()
    }

    @Test fun clearList() {
        val firstItem = NoteEntity(id = Random.nextLong(), type = NoteType.TEXT)
        val secondItem = NoteEntity(id = Random.nextLong(), type = NoteType.ROLL)

        val removeList = listOf(firstItem, secondItem)

        val endNoteList = MutableList(size = 5) { NoteEntity(id = Random.nextLong()) }
        val endRollList = MutableList(size = 5) {
            RollEntity(id = Random.nextLong(), noteId = Random.nextLong())
        }
        val endRollVisibleList = MutableList(size = 5) {
            RollVisibleEntity(id = Random.nextLong(), noteId = Random.nextLong())
        }
        val endRankList = MutableList(size = 5) {
            RankEntity(id = Random.nextLong(), noteId = MutableList(size = 5) { Random.nextLong() })
        }
        val endAlarmList = MutableList(size = 5) {
            AlarmEntity(id = Random.nextLong(), noteId = Random.nextLong())
        }

        val endModel = BackupRepo.Model(
            endNoteList, endRollList, endRollVisibleList, endRankList, endAlarmList
        )

        val startNoteList = endNoteList.map { it.copy() }.toMutableList()
        val startRollList = endRollList.map { it.copy() }.toMutableList()
        val startRollVisibleList = endRollVisibleList.map { it.copy() }.toMutableList()
        val startRankList = endRankList.map { it.copy() }.toMutableList()
        val startAlarmList = endAlarmList.map { it.copy() }.toMutableList()

        startNoteList.addAll(removeList)
        startRollList.addAll(
            List(size = 5) { RollEntity(id = Random.nextLong(), noteId = secondItem.id) }
        )
        startRollVisibleList.addAll(
            List(size = 5) { RollVisibleEntity(id = Random.nextLong(), noteId = secondItem.id) }
        )
        for ((i, item) in startRankList.withIndex()) {
            if (i.isDivideTwoEntirely()) {
                val id = if (Random.nextBoolean()) firstItem.id else secondItem.id
                item.noteId.add(id)
            }
        }

        startAlarmList.addAll(
            List(size = 5) {
                val noteId = if (Random.nextBoolean()) firstItem.id else secondItem.id
                return@List AlarmEntity(id = Random.nextLong(), noteId = noteId)
            }
        )

        val startModel = BackupRepo.Model(
            startNoteList, startRollList, startRollVisibleList, startRankList, startAlarmList
        )

        assertNotEquals(startModel, endModel)
        backupRepo.clearList(removeList, startModel)
        assertEquals(startModel, endModel)
    }

    @Test fun clearRankList() = startCoTest {
        val existList = List(size = 5) { RankEntity(id = Random.nextLong(), name = nextString()) }

        coEvery { rankDao.get() } returns existList

        val existFirstItem = existList.first()
        val existSecondItem = existList.last()
        val removeFirstItem = existFirstItem.copy(id = Random.nextLong())
        val removeSecondItem = existSecondItem.copy(id = Random.nextLong())

        val resultRankList = List(size = 5) {
            RankEntity(id = Random.nextLong(), name = nextString())
        }
        val resultNoteList = List(size = 5) {
            NoteEntity(rankId = Random.nextLong(), rankPs = Random.nextInt())
        }.apply {
            first().rankId = existFirstItem.id
            first().rankPs = existList.indexOf(existFirstItem)
            last().rankId = existSecondItem.id
            last().rankPs = existList.indexOf(existSecondItem)
        }

        val rankList = resultRankList.map { it.copy() }.toMutableList().apply {
            add(removeFirstItem)
            add(removeSecondItem)
        }

        val noteList = resultNoteList.map { it.copy() }.toMutableList().apply {
            first().rankId = removeFirstItem.id
            first().rankPs = Random.nextInt()
            last().rankId = removeSecondItem.id
            last().rankPs = Random.nextInt()
        }

        val model = BackupRepo.Model(noteList, mockk(), mockk(), rankList, mockk())

        assertNotEquals(model.noteList, resultNoteList)
        assertNotEquals(model.rankList, resultRankList)

        backupRepo.clearRankList(model, roomDb)

        assertEquals(model.noteList, resultNoteList)
        assertEquals(model.rankList, resultRankList)

        coVerifySequence {
            roomDb.rankDao
            rankDao.get()
        }
    }

    @Test fun clearAlarmList() = startCoTest {
        val existList = mockk<List<NotificationItem>>()

        coEvery { alarmDao.getList() } returns existList
        every { spyBackupRepo.moveNotificationTime(any(), any(), existList) } returns Unit

        val resultAlarmList = List(size = 5) {
            AlarmEntity(id = Random.nextLong(), date = getRandomFutureTime())
        }

        val alarmList = resultAlarmList.map { it.copy() }.toMutableList().apply {
            add(AlarmEntity(id = Random.nextLong(), date = getRandomPastTime()))
            add(AlarmEntity(id = Random.nextLong(), date = getRandomPastTime()))
        }

        val model = BackupRepo.Model(mockk(), mockk(), mockk(), mockk(), alarmList)

        assertNotEquals(model.alarmList, resultAlarmList)

        spyBackupRepo.clearAlarmList(model, roomDb)

        assertEquals(model.alarmList, resultAlarmList)

        coVerifySequence {
            spyBackupRepo.clearAlarmList(model, roomDb)

            roomDb.alarmDao
            alarmDao.getList()

            for (it in resultAlarmList) {
                spyBackupRepo.moveNotificationTime(it, any(), existList)
            }
        }
    }

    @Test fun moveNotificationTime() {
        val list = List(size = 5) {
            val type = NoteType.values().random()
            val color = Color.values().random()
            return@List NotificationItem(
                NotificationItem.Note(Random.nextLong(), nextString(), color, type),
                NotificationItem.Alarm(Random.nextLong(), getCalendarWithAdd(it).getText())
            )
        }

        val startCalendar = getCalendarWithAdd(min = 1)
        val resultCalendar = getCalendarWithAdd(min = list.size)
        val resultDate = resultCalendar.getText()
        val item = AlarmEntity(date = startCalendar.getText())

        assertNotEquals(item.date, resultDate)
        assertNotEquals(startCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))

        backupRepo.moveNotificationTime(item, startCalendar, list)

        assertEquals(item.date, resultDate)
        assertEquals(startCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))
    }

    @Test fun insertNoteList() {
        TODO()
    }

    @Test fun insertRollList() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val item = mockk<RollEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.rollList } returns mutableListOf(item)
        coEvery { rollDao.insert(item) } returns newId

        backupRepo.insertRollList(model, roomDb)

        coVerifySequence {
            model.rollList

            roomDb.rollDao
            item.id = Roll.Default.ID
            rollDao.insert(item)
            item.id = newId
        }
    }

    @Test fun insertRollVisibleList() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val item = mockk<RollVisibleEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.rollVisibleList } returns mutableListOf(item)
        coEvery { rollVisibleDao.insert(item) } returns newId

        backupRepo.insertRollVisibleList(model, roomDb)

        coVerifySequence {
            model.rollVisibleList

            roomDb.rollVisibleDao
            item.id = RollVisible.Default.ID
            rollVisibleDao.insert(item)
            item.id = newId
        }
    }

    @Test fun insertRankList() {
        TODO()
    }

    @Test fun insertAlarmList() = startCoTest {
        val model = mockk<BackupRepo.Model>()
        val item = mockk<AlarmEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.alarmList } returns mutableListOf(item)
        coEvery { alarmDao.insert(item) } returns newId

        backupRepo.insertAlarmList(model, roomDb)

        coVerifySequence {
            model.alarmList

            roomDb.alarmDao
            item.id = Alarm.Default.ID
            alarmDao.insert(item)
            item.id = newId
        }
    }
}