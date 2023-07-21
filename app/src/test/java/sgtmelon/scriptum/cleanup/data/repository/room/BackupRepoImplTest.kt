package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.database.DbData.Alarm
import sgtmelon.scriptum.infrastructure.database.DbData.Roll
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.test.common.getRandomFutureTime
import sgtmelon.test.common.getRandomPastTime
import sgtmelon.test.common.getRandomSize
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString
import java.util.Calendar
import kotlin.random.Random

/**
 * Test for [BackupRepoImpl].
 */
class BackupRepoImplTest : ParentRepoTest() {

    private val repository by lazy {
        BackupRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource
        )
    }
    private val spyRepository by lazy { spyk(repository) }

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

        val parserResult = ParserResult.Export(
            noteList, rollList, rollVisibleList, rankList, alarmList
        )

        coEvery { noteDataSource.getList(isBin = false) } returns noteList
        coEvery { rollDataSource.getList(noteIdList) } returns rollList
        coEvery { rollVisibleDataSource.getList(noteIdList) } returns rollVisibleList
        coEvery { rankDataSource.getList() } returns rankList
        coEvery { alarmDataSource.getList(noteIdList) } returns alarmList

        runBlocking {
            assertEquals(repository.getData(), parserResult)
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
        val result = mockk<ParserResult.Import>()
        val noteList = mockk<MutableList<NoteEntity>>()
        val size = Random.nextInt()
        val removeList = mockk<List<NoteEntity>>()

        every { result.noteList } returns noteList
        every { noteList.size } returns size

        coEvery { spyRepository.getRemoveNoteList(result) } returns removeList
        coEvery { spyRepository.clearList(removeList, result) } returns Unit
        coEvery { spyRepository.clearRankList(result) } returns Unit
        coEvery { spyRepository.clearAlarmList(result) } returns Unit
        coEvery { spyRepository.insertNoteList(result) } returns Unit
        coEvery { spyRepository.insertRollList(result) } returns Unit
        coEvery { spyRepository.insertRollVisibleList(result) } returns Unit
        coEvery { spyRepository.insertRankList(result) } returns Unit
        coEvery { spyRepository.insertAlarmList(result) } returns Unit

        runBlocking {
            assertEquals(
                spyRepository.insertData(result, isSkipImports = false),
                ImportResult.Simple
            )
        }

        val skipResult = ImportResult.Skip(skipCount = 0)
        runBlocking {
            assertEquals(spyRepository.insertData(result, isSkipImports = true), skipResult)
        }

        coVerifyOrder {
            spyRepository.insertData(result, isSkipImports = false)
            result.noteList
            noteList.size
            spyRepository.clearRankList(result)
            spyRepository.clearAlarmList(result)
            spyRepository.insertNoteList(result)
            spyRepository.insertRollList(result)
            spyRepository.insertRollVisibleList(result)
            spyRepository.insertRankList(result)
            spyRepository.insertAlarmList(result)

            spyRepository.insertData(result, isSkipImports = true)
            result.noteList
            noteList.size
            spyRepository.getRemoveNoteList(result)
            spyRepository.clearList(removeList, result)
            spyRepository.clearRankList(result)
            spyRepository.clearAlarmList(result)
            spyRepository.insertNoteList(result)
            spyRepository.insertRollList(result)
            spyRepository.insertRollVisibleList(result)
            spyRepository.insertRankList(result)
            spyRepository.insertAlarmList(result)
            result.noteList
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

        val list = MutableList(getRandomSize()) {
            NoteEntity(name = nextShortString(), text = nextString())
        }

        every { item.name } returns name
        every { item.text } returns text

        assertFalse(repository.needSkipTextNote(item, list))

        list.add(item)

        assertTrue(repository.needSkipTextNote(item, list))
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

        val endResult = ParserResult.Import(
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

        val startResult = ParserResult.Import(
            startNoteList, startRollList, startRollVisibleList, startRankList, startAlarmList
        )

        assertNotEquals(startResult, endResult)
        repository.clearList(removeList, startResult)
        assertEquals(startResult, endResult)
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

        val parserResult = ParserResult.Import(noteList, mockk(), mockk(), rankList, mockk())

        assertNotEquals(parserResult.noteList, resultNoteList)
        assertNotEquals(parserResult.rankList, resultRankList)

        runBlocking {
            repository.clearRankList(parserResult)
        }

        assertEquals(parserResult.noteList, resultNoteList)
        assertEquals(parserResult.rankList, resultRankList)

        coVerifySequence {
            rankDataSource.getList()
        }
    }

    @Test fun clearAlarmList() {
        val existList = mockk<List<NotificationItem>>()

        coEvery { alarmDataSource.getItemList() } returns existList
        every { spyRepository.moveNotificationTime(any(), any(), existList) } returns Unit

        val resultAlarmList = List(size = 5) {
            AlarmEntity(id = Random.nextLong(), date = getRandomFutureTime())
        }

        val alarmList = resultAlarmList.map { it.copy() }.toMutableList().apply {
            add(AlarmEntity(id = Random.nextLong(), date = getRandomPastTime()))
            add(AlarmEntity(id = Random.nextLong(), date = getRandomPastTime()))
        }

        val parserResult = ParserResult.Import(mockk(), mockk(), mockk(), mockk(), alarmList)

        assertNotEquals(parserResult.alarmList, resultAlarmList)

        runBlocking {
            spyRepository.clearAlarmList(parserResult)
        }

        assertEquals(parserResult.alarmList, resultAlarmList)

        coVerifySequence {
            spyRepository.clearAlarmList(parserResult)

            alarmDataSource.getItemList()

            for (it in resultAlarmList) {
                spyRepository.moveNotificationTime(it, any(), existList)
            }
        }
    }

    @Test fun moveNotificationTime() {
        val list = List(size = 5) {
            val type = NoteType.values().random()
            val color = Color.values().random()
            return@List NotificationItem(
                NotificationItem.Note(Random.nextLong(), nextString(), color, type),
                NotificationItem.Alarm(Random.nextLong(), getClearCalendar(it).toText())
            )
        }

        val startCalendar = getClearCalendar(addMinutes = 1)
        val resultCalendar = getClearCalendar(addMinutes = list.size)
        val resultDate = resultCalendar.toText()
        val item = AlarmEntity(date = startCalendar.toText())

        assertNotEquals(item.date, resultDate)
        assertNotEquals(startCalendar.get(Calendar.MINUTE), resultCalendar.get(Calendar.MINUTE))

        repository.moveNotificationTime(item, startCalendar, list)

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
        val result = mockk<ParserResult.Import>()
        val item = mockk<RollEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { result.rollList } returns mutableListOf(item)
        coEvery { rollDataSource.insert(item) } returns newId

        runBlocking {
            repository.insertRollList(result)
        }

        coVerifySequence {
            result.rollList

            item.id = Roll.Default.ID
            rollDataSource.insert(item)
            item.id = newId
        }
    }

    // TODO #CHECK + null case
    @Test fun insertRollVisibleList() {
        val result = mockk<ParserResult.Import>()
        val item = mockk<RollVisibleEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { result.rollVisibleList } returns mutableListOf(item)
        coEvery { rollVisibleDataSource.insert(item) } returns newId

        runBlocking {
            repository.insertRollVisibleList(result)
        }

        coVerifySequence {
            result.rollVisibleList

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
        val result = mockk<ParserResult.Import>()
        val item = mockk<AlarmEntity>(relaxUnitFun = true)
        val newId = Random.nextLong()

        every { result.alarmList } returns mutableListOf(item)
        coEvery { alarmDataSource.insert(item) } returns newId

        runBlocking {
            repository.insertAlarmList(result)
        }

        coVerifySequence {
            result.alarmList

            item.id = Alarm.Default.ID
            alarmDataSource.insert(item)
            item.id = newId
        }
    }

    //endregion
}