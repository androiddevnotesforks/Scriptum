package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getRandomFutureTime
import sgtmelon.common.utils.getRandomPastTime
import sgtmelon.common.utils.getText
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
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString

/**
 * Test for [BackupRepoImpl].
 */
class BackupRepoImplTest : ParentRepoTest() {

    private val repo by lazy {
        BackupRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource
        )
    }
    private val spyRepo by lazy { spyk(repo) }

    @Test fun getData() {
        val noteList = listOf(
            NoteEntity(id = Random.nextLong(), type = NoteType.TEXT),
            NoteEntity(id = Random.nextLong(), type = NoteType.ROLL),
            NoteEntity(id = Random.nextLong(), type = NoteType.ROLL),
            NoteEntity(id = Random.nextLong(), type = NoteType.TEXT)
        )

        val noteIdList = noteList.filter { it.type == NoteType.ROLL }.map { it.id }

        val rollList = mockk<List<RollEntity>>()
        val rollVisibleList = mockk<List<RollVisibleEntity>>()
        val rankList = mockk<List<RankEntity>>()
        val alarmList = mockk<List<AlarmEntity>>()

        val parserResult = ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)

        coEvery { noteDataSource.getList(isBin = false) } returns noteList
        coEvery { rollDataSource.getList(noteIdList) } returns rollList
        coEvery { rollVisibleDataSource.getList(noteIdList) } returns rollVisibleList
        coEvery { rankDataSource.getList() } returns rankList
        coEvery { alarmDataSource.getList(noteIdList) } returns alarmList

        runBlocking {
            assertEquals(repo.getData(), parserResult)
        }

        coVerifySequence {
            noteDataSource.getList(isBin = false)
            rollDataSource.getList(noteIdList)
            rollVisibleDataSource.getList(noteIdList)
            rankDataSource.getList()
            alarmDataSource.getList(noteIdList)
        }
    }

    //region Insert functions

    @Test fun insertData() {
        val model = mockk<BackupRepoImpl.Model>()
        val noteList = mockk<MutableList<NoteEntity>>()
        val size = Random.nextInt()
        val removeList = mockk<List<NoteEntity>>()

        every { model.noteList } returns noteList
        every { noteList.size } returns size

        coEvery { spyRepo.getRemoveNoteList(model) } returns removeList
        coEvery { spyRepo.clearList(removeList, model) } returns Unit
        coEvery { spyRepo.clearRankList(model) } returns Unit
        coEvery { spyRepo.clearAlarmList(model) } returns Unit
        coEvery { spyRepo.insertNoteList(model) } returns Unit
        coEvery { spyRepo.insertRollList(model) } returns Unit
        coEvery { spyRepo.insertRollVisibleList(model) } returns Unit
        coEvery { spyRepo.insertRankList(model) } returns Unit
        coEvery { spyRepo.insertAlarmList(model) } returns Unit

        runBlocking {
            assertEquals(
                spyRepo.insertData(model, isSkipImports = false),
                ImportResult.Simple
            )
        }

        val skipResult = ImportResult.Skip(skipCount = 0)
        runBlocking {
            assertEquals(spyRepo.insertData(model, isSkipImports = true), skipResult)
        }

        coVerifyOrder {
            spyRepo.insertData(model, isSkipImports = false)
            model.noteList
            noteList.size
            spyRepo.clearRankList(model)
            spyRepo.clearAlarmList(model)
            spyRepo.insertNoteList(model)
            spyRepo.insertRollList(model)
            spyRepo.insertRollVisibleList(model)
            spyRepo.insertRankList(model)
            spyRepo.insertAlarmList(model)

            spyRepo.insertData(model, isSkipImports = true)
            model.noteList
            noteList.size
            spyRepo.getRemoveNoteList(model)
            spyRepo.clearList(removeList, model)
            spyRepo.clearRankList(model)
            spyRepo.clearAlarmList(model)
            spyRepo.insertNoteList(model)
            spyRepo.insertRollList(model)
            spyRepo.insertRollVisibleList(model)
            spyRepo.insertRankList(model)
            spyRepo.insertAlarmList(model)
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

        assertFalse(repo.needSkipTextNote(item, list))

        list.add(item)

        assertTrue(repo.needSkipTextNote(item, list))
    }

    @Test fun needSkipRollNote() {
        TODO()
    }

    @Test fun isContainSameItems() {
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

        val endModel = BackupRepoImpl.Model(
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
            if (i.isDivideEntirely()) {
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

        val startModel = BackupRepoImpl.Model(
            startNoteList, startRollList, startRollVisibleList, startRankList, startAlarmList
        )

        assertNotEquals(startModel, endModel)
        repo.clearList(removeList, startModel)
        assertEquals(startModel, endModel)
    }

    @Test fun clearRankList() {
        val existList = List(size = 5) { RankEntity(id = Random.nextLong(), name = nextString()) }

        coEvery { rankDataSource.getList() } returns existList

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

        val model = BackupRepoImpl.Model(noteList, mockk(), mockk(), rankList, mockk())

        assertNotEquals(model.noteList, resultNoteList)
        assertNotEquals(model.rankList, resultRankList)

        runBlocking {
            repo.clearRankList(model)
        }

        assertEquals(model.noteList, resultNoteList)
        assertEquals(model.rankList, resultRankList)

        coVerifySequence {
            rankDataSource.getList()
        }
    }

    @Test fun clearAlarmList() {
        val existList = mockk<List<NotificationItem>>()

        coEvery { alarmDataSource.getItemList() } returns existList
        every { spyRepo.moveNotificationTime(any(), any(), existList) } returns Unit

        val resultAlarmList = List(size = 5) {
            AlarmEntity(id = Random.nextLong(), date = getRandomFutureTime())
        }

        val alarmList = resultAlarmList.map { it.copy() }.toMutableList().apply {
            add(AlarmEntity(id = Random.nextLong(), date = getRandomPastTime()))
            add(AlarmEntity(id = Random.nextLong(), date = getRandomPastTime()))
        }

        val model = BackupRepoImpl.Model(mockk(), mockk(), mockk(), mockk(), alarmList)

        assertNotEquals(model.alarmList, resultAlarmList)

        runBlocking {
            spyRepo.clearAlarmList(model)
        }

        assertEquals(model.alarmList, resultAlarmList)

        coVerifySequence {
            spyRepo.clearAlarmList(model)

            alarmDataSource.getItemList()

            for (it in resultAlarmList) {
                spyRepo.moveNotificationTime(it, any(), existList)
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

        repo.moveNotificationTime(item, startCalendar, list)

        assertEquals(item.date, resultDate)
        assertEquals(startCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))
    }

    @Test fun insertNoteList() {
        TODO()
    }

    @Test fun updateRollLink() {
        TODO()
    }

    @Test fun updateRollVisibleLink() {
        TODO()
    }

    @Test fun updateRankLink() {
        TODO()
    }

    @Test fun updateAlarmList() {
        TODO()
    }

    // TODO #CHECK + null case
    @Test fun insertRollList() {
        val model = mockk<BackupRepoImpl.Model>()
        val item = mockk<RollEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.rollList } returns mutableListOf(item)
        coEvery { rollDataSource.insert(item) } returns newId

        runBlocking {
            repo.insertRollList(model)
        }

        coVerifySequence {
            model.rollList

            item.id = Roll.Default.ID
            rollDataSource.insert(item)
            item.id = newId
        }
    }

    // TODO #CHECK + null case
    @Test fun insertRollVisibleList() {
        val model = mockk<BackupRepoImpl.Model>()
        val item = mockk<RollVisibleEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.rollVisibleList } returns mutableListOf(item)
        coEvery { rollVisibleDataSource.insert(item) } returns newId

        runBlocking {
            repo.insertRollVisibleList(model)
        }

        coVerifySequence {
            model.rollVisibleList

            item.id = RollVisible.Default.ID
            rollVisibleDataSource.insert(item)
            item.id = newId
        }
    }

    @Test fun insertRankList() {
        TODO()
    }

    // TODO #CHECK + null case
    @Test fun insertAlarmList() {
        val model = mockk<BackupRepoImpl.Model>()
        val item = mockk<AlarmEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { model.alarmList } returns mutableListOf(item)
        coEvery { alarmDataSource.insert(item) } returns newId

        runBlocking {
            repo.insertAlarmList(model)
        }

        coVerifySequence {
            model.alarmList

            item.id = Alarm.Default.ID
            alarmDataSource.insert(item)
            item.id = newId
        }
    }

    //endregion
}