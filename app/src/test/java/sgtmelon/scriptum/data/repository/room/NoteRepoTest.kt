package sgtmelon.scriptum.data.repository.room

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RollConverter
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.copy
import sgtmelon.scriptum.extension.move
import kotlin.random.Random

/**
 * Test for [NoteRepo].
 */
@Suppress("UnusedEquals")
@ExperimentalCoroutinesApi
class NoteRepoTest : ParentRoomRepoTest() {

    private val noteConverter = mockk<NoteConverter>()
    private val rollConverter = mockk<RollConverter>()

    private val mockNoteRepo by lazy { NoteRepo(roomProvider, noteConverter, rollConverter) }
    private val noteRepo by lazy { NoteRepo(roomProvider, NoteConverter(), RollConverter()) }
    private val spyNoteRepo by lazy { spyk(mockNoteRepo) }

    override fun tearDown() {
        super.tearDown()

        confirmVerified(noteConverter, rollConverter)
    }


    @Test fun getCount() = startCoTest {
        val notesCount = Random.nextInt()
        val binCount = Random.nextInt()

        val rankIdVisibleList = List(size = 2) { Random.nextLong() }
        val rankIdList = List(size = 5) { Random.nextLong() }

        coEvery { rankDao.getIdVisibleList() } returns rankIdVisibleList
        coEvery { noteDao.getCount(bin = false, rankIdList = rankIdVisibleList) } returns notesCount
        assertEquals(notesCount, mockNoteRepo.getCount(bin = false))

        coEvery { rankDao.getIdList() } returns rankIdList
        coEvery { noteDao.getCount(bin = true, rankIdList = rankIdList) } returns binCount
        assertEquals(binCount, mockNoteRepo.getCount(bin = true))

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getIdVisibleList()
            noteDao.getCount(bin = false, rankIdList = rankIdVisibleList)

            roomProvider.openRoom()
            rankDao.getIdList()
            noteDao.getCount(bin = true, rankIdList = rankIdList)
        }
    }

    @Test fun getList() = startCoTest {
        val sort = Sort.RANK
        val bin = Random.nextBoolean()
        val isOptimal = Random.nextBoolean()

        val entityList = NoteConverter().toEntity(TestData.Note.itemList)
        val entityFilterList = NoteConverter().toEntity(TestData.Note.itemList.filter {
            it.isVisible(listOf())
        })

        val finishList = TestData.Note.itemList.apply {
            forEach {
                it.alarmId = DbData.Alarm.Default.ID
                it.alarmDate = DbData.Alarm.Default.DATE
            }
        }
        val finishFilterList = finishList.filter { it.isVisible(listOf()) }

        mockNoteRepo.correctRankSort(finishList, Sort.RANK)

        coEvery { noteDao.getByRank(bin) } returns entityList
        coEvery { rankDao.getIdVisibleList() } returns listOf()
        coEvery {
            with(rollDao) { if (isOptimal) getView(any()) else get(any()) }
        } returns mutableListOf()
        coEvery { alarmDao.get(noteId = any()) } returns null

        assertEquals(finishList, noteRepo.getList(sort, bin, isOptimal, filterVisible = false))
        assertEquals(
                finishFilterList, noteRepo.getList(sort, bin, isOptimal, filterVisible = true)
        )

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.getByRank(bin)
            entityList.forEach {
                if (isOptimal) rollDao.getView(it.id) else rollDao.get(it.id)
                alarmDao.get(it.id)
            }

            roomProvider.openRoom()
            noteDao.getByRank(bin)
            rankDao.getIdVisibleList()
            entityFilterList.forEach {
                if (isOptimal) rollDao.getView(it.id) else rollDao.get(it.id)
                alarmDao.get(it.id)
            }
        }
    }

    @Test fun getSortBy() = startCoTest {
        val changeList = MutableList(size = 5) { NoteEntity(id = Random.nextLong()) }
        val createList = MutableList(size = 5) { NoteEntity(id = Random.nextLong()) }
        val rankList = MutableList(size = 5) { NoteEntity(id = Random.nextLong()) }
        val colorList = MutableList(size = 5) { NoteEntity(id = Random.nextLong()) }

        assertEquals(listOf<NoteEntity>(), mockNoteRepo.getSortBy(noteDao, sort = 10, bin = false))
        assertEquals(listOf<NoteEntity>(), mockNoteRepo.getSortBy(noteDao, sort = 10, bin = true))

        coEvery { noteDao.getByChange(any()) } returns changeList
        assertEquals(changeList, mockNoteRepo.getSortBy(noteDao, Sort.CHANGE, bin = false))
        assertEquals(changeList, mockNoteRepo.getSortBy(noteDao, Sort.CHANGE, bin = true))

        coEvery { noteDao.getByCreate(any()) } returns createList
        assertEquals(createList, mockNoteRepo.getSortBy(noteDao, Sort.CREATE, bin = false))
        assertEquals(createList, mockNoteRepo.getSortBy(noteDao, Sort.CREATE, bin = true))

        coEvery { noteDao.getByRank(any()) } returns rankList
        assertEquals(rankList, mockNoteRepo.getSortBy(noteDao, Sort.RANK, bin = false))
        assertEquals(rankList, mockNoteRepo.getSortBy(noteDao, Sort.RANK, bin = true))

        coEvery { noteDao.getByColor(any()) } returns colorList
        assertEquals(colorList, mockNoteRepo.getSortBy(noteDao, Sort.COLOR, bin = false))
        assertEquals(colorList, mockNoteRepo.getSortBy(noteDao, Sort.COLOR, bin = true))

        coVerifySequence {
            noteDao.getByChange(bin = false)
            noteDao.getByChange(bin = true)

            noteDao.getByCreate(bin = false)
            noteDao.getByCreate(bin = true)

            noteDao.getByRank(bin = false)
            noteDao.getByRank(bin = true)

            noteDao.getByColor(bin = false)
            noteDao.getByColor(bin = true)
        }
    }

    @Test fun filterVisible() = startCoTest {
        val entityList = NoteConverter().toEntity(TestData.Note.itemList)
        val idVisibleList = listOf(1L)

        entityList.forEach {
            every { noteConverter.toItem(it) } returns NoteConverter().toItem(it)
        }

        coEvery { rankDao.getIdVisibleList() } returns idVisibleList

        assertEquals(entityList.subList(0, 3), mockNoteRepo.filterVisible(rankDao, entityList))

        coVerifySequence {
            rankDao.getIdVisibleList()
            entityList.forEach {
                noteConverter.toItem(it)
            }
        }
    }

    @Test fun correctRankSort() {
        val startList = TestData.Note.itemList
        val finishList = TestData.Note.itemList.apply { move(from = 0) }
        val simpleList: MutableList<NoteItem> = MutableList(size = 5) {
            TestData.Note.firstNote.deepCopy(id = Random.nextLong())
        }

        assertNotEquals(finishList, mockNoteRepo.correctRankSort(startList, Sort.COLOR))
        assertEquals(finishList, mockNoteRepo.correctRankSort(startList, Sort.RANK))
        assertEquals(simpleList, mockNoteRepo.correctRankSort(simpleList, Sort.RANK))
    }


    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()

        val noteEntity = mockk<NoteEntity>()
        val noteItem = mockk<NoteItem>()
        val rollEntityList = mockk<MutableList<RollEntity>>()
        val rollItemList = mockk<MutableList<RollItem>>()
        val alarmEntity = mockk<AlarmEntity>()

        coEvery { noteDao.get(id) } returns null
        assertNull(mockNoteRepo.getItem(id, isOptimal = Random.nextBoolean()))

        coEvery { noteDao.get(id) } returns noteEntity
        coEvery { spyNoteRepo.getPreview(rollDao, id, any()) } returns rollEntityList
        every { rollConverter.toItem(rollEntityList) } returns rollItemList
        coEvery { alarmDao.get(id) } returns alarmEntity
        every { noteConverter.toItem(noteEntity, rollItemList, alarmEntity) } returns noteItem

        assertEquals(noteItem, spyNoteRepo.getItem(id, isOptimal = false))
        assertEquals(noteItem, spyNoteRepo.getItem(id, isOptimal = true))

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.get(id)

            spyNoteRepo.getItem(id, isOptimal = false)
            spyNoteRepo.takeFromRoom<NoteItem?>(any())
            spyNoteRepo.roomProvider
            roomProvider.openRoom()
            noteDao.get(id)
            spyNoteRepo.getPreview(rollDao, id, isOptimal = false)
            rollConverter.toItem(rollEntityList)
            alarmDao.get(id)
            noteConverter.toItem(noteEntity, rollItemList, alarmEntity)

            spyNoteRepo.getItem(id, isOptimal = true)
            spyNoteRepo.takeFromRoom<NoteItem?>(any())
            spyNoteRepo.roomProvider
            roomProvider.openRoom()
            noteDao.get(id)
            spyNoteRepo.getPreview(rollDao, id, isOptimal = true)
            rollConverter.toItem(rollEntityList)
            alarmDao.get(id)
            noteConverter.toItem(noteEntity, rollItemList, alarmEntity)
        }
    }

    @Test fun getPreview() = startCoTest {
        val id = Random.nextLong()

        val firstList = MutableList(size = 5) { RollEntity(id = Random.nextLong()) }
        val secondList = MutableList(size = 5) { RollEntity(id = Random.nextLong()) }

        coEvery { rollDao.get(id) } returns firstList
        assertEquals(firstList, mockNoteRepo.getPreview(rollDao, id, isOptimal = false))

        coEvery { rollDao.getView(id) } returns secondList
        assertEquals(secondList, mockNoteRepo.getPreview(rollDao, id, isOptimal = true))

        coVerifySequence {
            rollDao.get(id)
            rollDao.getView(id)
        }
    }


    @Test fun getRollList() = startCoTest {
        val noteId = Random.nextLong()

        val itemList = TestData.Note.rollList
        val entityList = RollConverter().toEntity(noteId, itemList)

        every { rollConverter.toItem(entityList) } returns itemList
        coEvery { rollDao.get(noteId) } returns entityList

        assertEquals(itemList, mockNoteRepo.getRollList(noteId))

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.get(noteId)
            rollConverter.toItem(entityList)
        }
    }


    @Test fun isListHide() = startCoTest {
        val idList = listOf<Long>(1, 2, 3)

        coEvery { rankDao.getIdVisibleList() } returns idList

        val entityList = mutableListOf<NoteEntity>()

        var entity = NoteEntity()
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, mockNoteRepo.isListHide())

        entity = NoteEntity(rankId = 0)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, mockNoteRepo.isListHide())

        entity = NoteEntity(rankPs = 0)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, mockNoteRepo.isListHide())

        entity = NoteEntity(rankId = 0, rankPs = 0)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(true, mockNoteRepo.isListHide())

        entity = NoteEntity(rankId = 1, rankPs = 1)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, mockNoteRepo.isListHide())

        coVerifySequence {
            entityList.forEach {
                roomProvider.openRoom()
                noteDao.get(false)
                noteConverter.toItem(it)
                rankDao.getIdVisibleList()
            }
        }
    }

    @Test fun clearBin() = startCoTest {
        val itemList = List<NoteEntity>(size = 5) {
            mockk {
                every { id } returns it.toLong()
                every { rankId } returns (it * it).toLong()
            }
        }

        coEvery { noteDao.get(true) } returns itemList
        coEvery { spyNoteRepo.clearConnection(rankDao, any(), any()) } returns Unit

        spyNoteRepo.clearBin()

        coVerifySequence {
            spyNoteRepo.clearBin()
            spyNoteRepo.inRoom(any())
            spyNoteRepo.roomProvider

            roomProvider.openRoom()
            noteDao.get(true)
            itemList.forEachIndexed { i, it ->
                it.id
                it.rankId
                spyNoteRepo.clearConnection(rankDao, i.toLong(), (i * i).toLong())
            }
            noteDao.delete(itemList)
        }
    }


    @Test fun deleteNote() = startCoTest {
        val startItem = TestData.Note.firstNote
        val finalItem = startItem.deepCopy().onDelete()

        val entity = NoteConverter().toEntity(finalItem)

        every { noteConverter.toEntity(finalItem) } returns entity

        mockNoteRepo.deleteNote(startItem)

        assertEquals(startItem, finalItem)

        coVerifySequence {
            roomProvider.openRoom()
            alarmDao.delete(startItem.id)
            noteConverter.toEntity(finalItem)
            noteDao.update(entity)
        }
    }

    @Test fun restoreNote() = startCoTest {
        val startItem = TestData.Note.firstNote
        val finalItem = startItem.deepCopy().onRestore()

        val entity = NoteConverter().toEntity(finalItem)

        every { noteConverter.toEntity(finalItem) } returns entity

        mockNoteRepo.restoreNote(startItem)

        assertEquals(startItem, finalItem)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(finalItem)
            noteDao.update(entity)
        }
    }

    @Test fun clearNote() = startCoTest {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()
        val rankId = Random.nextLong()
        val entity = mockk<NoteEntity>()

        every { item.id } returns id
        every { item.rankId } returns rankId
        coEvery { spyNoteRepo.clearConnection(rankDao, id, rankId) } returns Unit
        every { noteConverter.toEntity(item) } returns entity

        spyNoteRepo.clearNote(item)

        coVerifySequence {
            spyNoteRepo.clearNote(item)
            spyNoteRepo.inRoom(any())
            spyNoteRepo.roomProvider

            roomProvider.openRoom()
            item.id
            item.rankId
            spyNoteRepo.clearConnection(rankDao, id, rankId)
            noteConverter.toEntity(item)
            noteDao.delete(entity)
            roomDb.close()
        }
    }


    @Test fun convertNote_text() = startCoTest {
        val startItem = mockk<NoteItem.Text>()
        val finishItem = mockk<NoteItem.Roll>()
        val finishEntity = mockk<NoteEntity>()

        val rollItemList = mutableListOf<RollItem>(mockk(), mockk(), mockk())
        val rollEntity = mockk<RollEntity>()

        val noteId = Random.nextLong()
        val rollId = Random.nextLong()

        every { startItem.onConvert() } returns finishItem
        every { finishItem.list } returns rollItemList
        every { finishItem.id } returns noteId

        coEvery { rollDao.insert(rollEntity) } returns rollId
        every { rollConverter.toEntity(noteId, item = any()) } returns rollEntity
        rollItemList.forEach { every { it.id = rollId } returns Unit }

        every { noteConverter.toEntity(finishItem) } returns finishEntity

        assertEquals(finishItem, mockNoteRepo.convertNote(startItem))

        coVerifySequence {
            roomProvider.openRoom()
            startItem.onConvert()
            finishItem.list
            rollItemList.forEach {
                finishItem.id

                rollConverter.toEntity(noteId, it)
                rollDao.insert(rollEntity)
                it.id = rollId
            }
            noteConverter.toEntity(finishItem)
            noteDao.update(finishEntity)

            finishItem == finishItem
        }

    }

    @Test fun convertNote_roll() = startCoTest {
        val itemList = mutableListOf(
                RollItem(position = 0, text = "0"),
                RollItem(position = 1, text = "1"),
                RollItem(position = 2, text = "2"),
                RollItem(position = 3, text = "3")
        )

        val rollItem = TestData.Note.secondNote
        rollItem.list.clearAdd(itemList)

        val entityList = RollConverter().toEntity(rollItem.id, itemList)

        val textItem = rollItem.onConvert()
        val textEntity = NoteConverter().toEntity(textItem)

        every { rollConverter.toItem(entityList) } returns itemList
        every { noteConverter.toEntity(textItem) } returns textEntity
        coEvery { rollDao.get(rollItem.id) } returns entityList

        assertEquals(textItem, mockNoteRepo.convertNote(rollItem, useCache = false))
        assertEquals(textItem, mockNoteRepo.convertNote(rollItem, useCache = true))

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.get(rollItem.id)
            rollConverter.toItem(entityList)
            noteConverter.toEntity(textItem)
            noteDao.update(textEntity)
            rollDao.delete(rollItem.id)

            roomProvider.openRoom()
            noteConverter.toEntity(textItem)
            noteDao.update(textEntity)
            rollDao.delete(rollItem.id)
        }
    }

    @Test fun getCopyText_text() = startCoTest {
        val firstItem = NoteItem.Text(text = "noteText", color = Random.nextInt())
        val firstText = firstItem.text

        val secondItem = firstItem.deepCopy(name = "noteName")
        val secondText = "${secondItem.name}\n${secondItem.text}"

        assertEquals(firstText, mockNoteRepo.getCopyText(firstItem))
        assertEquals(secondText, mockNoteRepo.getCopyText(secondItem))
    }

    @Test fun getCopyText_roll() = startCoTest {
        val entityList = mutableListOf(
                RollEntity(text = "1"), RollEntity(text = "2"), RollEntity(text = "3")
        )
        val itemList = RollConverter().toItem(entityList)

        val firstItem = NoteItem.Roll(id = Random.nextLong(), color = Random.nextInt())
        val firstText = itemList.joinToString(separator = "\n") { it.text }

        val secondItem = NoteItem.Roll(id = Random.nextLong(), name = "noteName", color = Random.nextInt())
        val secondText = "${secondItem.name}\n${itemList.joinToString(separator = "\n") { it.text }}"

        every { rollConverter.toItem(entityList) } returns itemList
        coEvery { rollDao.get(any()) } returns entityList

        assertEquals(firstText, mockNoteRepo.getCopyText(firstItem))
        assertEquals(secondText, mockNoteRepo.getCopyText(secondItem))

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.get(firstItem.id)
            rollConverter.toItem(entityList)

            roomProvider.openRoom()
            rollDao.get(secondItem.id)
            rollConverter.toItem(entityList)
        }
    }

    @Test fun saveNote_text() = startCoTest {
        val id = Random.nextLong()

        val startItem = TestData.Note.firstNote
        val finishItem = startItem.deepCopy(id = id)
        val entity = NoteConverter().toEntity(startItem)

        every { noteConverter.toEntity(startItem) } returns entity
        coEvery { noteDao.insert(entity) } returns id

        mockNoteRepo.saveNote(startItem, isCreate = false)
        assertNotEquals(startItem, finishItem)

        mockNoteRepo.saveNote(startItem, isCreate = true)
        assertEquals(startItem, finishItem)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(startItem)
            noteDao.update(entity)

            roomProvider.openRoom()
            noteConverter.toEntity(startItem)
            noteDao.insert(entity)
        }
    }

    @Test fun saveNote_roll_onCreate() = startCoTest {
        val id = Random.nextLong()

        val startList = TestData.Note.rollList
        val finishList = startList.copy()

        val startItem = TestData.Note.secondNote.deepCopy()
        val finishItem = startItem.deepCopy(id = id, list = finishList)

        startList.forEachIndexed { i, it ->
            it.id = null

            val entity = RollConverter().toEntity(id, it)

            every { rollConverter.toEntity(id, it) } returns entity
            coEvery { rollDao.insert(entity) } returns i.toLong()
        }

        startItem.list.clearAdd(startList)

        val entity = NoteConverter().toEntity(startItem)

        every { noteConverter.toEntity(startItem) } returns entity
        coEvery { noteDao.insert(entity) } returns id

        assertNotEquals(startItem, finishItem)

        mockNoteRepo.saveNote(startItem, isCreate = true)
        assertEquals(startItem.list, finishItem.list)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(startItem)
            noteDao.insert(entity)
            startItem.list.forEach {
                val rollEntity = RollConverter().toEntity(startItem.id, it.copy(id = null))

                rollConverter.toEntity(id, it)
                rollDao.insert(rollEntity)
            }
        }
    }

    @Test fun saveNote_roll_onUpdate() = startCoTest {
        val startList = TestData.Note.rollList
        val finishList = startList.copy()

        val startItem = TestData.Note.secondNote
        val finishItem = startItem.deepCopy(list = finishList)

        startList.forEachIndexed { i, it ->
            if (i % 2 == 0) {
                it.id = null

                val entity = RollConverter().toEntity(startItem.id, it)

                every { rollConverter.toEntity(startItem.id, it) } returns entity
                coEvery { rollDao.insert(entity) } returns i.toLong()
            }
        }

        startItem.list.clearAdd(startList)

        val entity = NoteConverter().toEntity(startItem)
        val idSaveList = finishList.mapNotNull { it.id }

        every { noteConverter.toEntity(startItem) } returns entity

        assertNotEquals(startItem.list, finishItem.list)
        mockNoteRepo.saveNote(startItem, isCreate = false)
        assertEquals(startItem, finishItem)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(startItem)
            noteDao.update(entity)
            startItem.list.forEachIndexed { i, it ->
                if (i % 2 == 0) {
                    val rollEntity = RollConverter().toEntity(startItem.id, it.copy(id = null))

                    rollConverter.toEntity(startItem.id, it)
                    rollDao.insert(rollEntity)
                } else {
                    val id = it.id ?: throw NullPointerException()
                    rollDao.update(id, it.position, it.text)
                }
            }
            rollDao.delete(startItem.id, idSaveList)
        }
    }

    @Test fun updateRollCheck_item() = startCoTest {
        val noteItem = TestData.Note.secondNote.apply {
            list.addAll(TestData.Note.rollList)
        }
        val noteEntity = NoteConverter().toEntity(noteItem)

        every { noteConverter.toEntity(noteItem) } returns noteEntity

        val p = noteItem.list.indices.random()
        val rollItem = noteItem.list[p]
        val rollId = Random.nextLong()

        mockNoteRepo.updateRollCheck(noteItem, Random.nextInt())

        rollItem.id = null
        mockNoteRepo.updateRollCheck(noteItem, p)

        rollItem.id = rollId
        mockNoteRepo.updateRollCheck(noteItem, p)

        coVerifySequence {
            roomProvider.openRoom()
            roomProvider.openRoom()

            roomProvider.openRoom()
            rollDao.update(rollId, rollItem.isCheck)
            noteConverter.toEntity(noteItem)
            noteDao.update(noteEntity)
        }
    }

    @Test fun updateRollCheck_list() = startCoTest {
        val item = TestData.Note.secondNote
        val entity = NoteConverter().toEntity(item)

        val check = Random.nextBoolean()

        every { noteConverter.toEntity(item) } returns entity

        mockNoteRepo.updateRollCheck(item, check)

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.updateAllCheck(item.id, check)
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }

    @Test fun updateNote() = startCoTest {
        val item = TestData.Note.itemList.random()
        val entity = NoteConverter().toEntity(item)

        every { noteConverter.toEntity(item) } returns entity

        mockNoteRepo.updateNote(item)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }


    @Test fun setRollVisible() = startCoTest {
        val id = Random.nextLong()
        val entity = RollVisibleEntity(noteId = id, value = false)

        coEvery { rollVisibleDao.get(id) } returns null
        coEvery { rollVisibleDao.insert(any()) } returns Random.nextLong()
        mockNoteRepo.setRollVisible(id, isVisible = false)

        coEvery { rollVisibleDao.get(id) } returns false
        mockNoteRepo.setRollVisible(id, isVisible = false)
        mockNoteRepo.setRollVisible(id, isVisible = true)

        coVerifySequence {
            roomProvider.openRoom()
            rollVisibleDao.get(id)
            rollVisibleDao.insert(entity)

            roomProvider.openRoom()
            rollVisibleDao.get(id)

            roomProvider.openRoom()
            rollVisibleDao.get(id)
            rollVisibleDao.update(id, value = true)
        }
    }

    @Test fun getRollVisible() = startCoTest {
        val id = Random.nextLong()
        val entity = RollVisibleEntity(noteId = id)

        coEvery { rollVisibleDao.get(id) } returns null
        coEvery { rollVisibleDao.insert(entity) } returns Random.nextLong()
        assertEquals(DbData.RollVisible.Default.VALUE, mockNoteRepo.getRollVisible(id))

        coEvery { rollVisibleDao.get(id) } returns false
        assertEquals(false, mockNoteRepo.getRollVisible(id))

        coEvery { rollVisibleDao.get(id) } returns true
        assertEquals(true, mockNoteRepo.getRollVisible(id))

        coVerifySequence {
            roomProvider.openRoom()
            rollVisibleDao.get(id)
            rollVisibleDao.insert(entity)

            roomProvider.openRoom()
            rollVisibleDao.get(id)

            roomProvider.openRoom()
            rollVisibleDao.get(id)
        }
    }


    @Test fun clearConnection() = startCoTest {
        val noteId = Random.nextLong()
        val rankId = Random.nextLong()

        val randomId = Random.nextLong()
        val startRankEntity = RankEntity(
                id = Random.nextLong(), noteId = mutableListOf(noteId, randomId),
                name = Random.nextString()
        )
        val finishRankEntity = startRankEntity.copy(noteId = mutableListOf(randomId))

        coEvery { rankDao.get(rankId) } returns null
        mockNoteRepo.clearConnection(rankDao, noteId, rankId)
        assertNotEquals(finishRankEntity, startRankEntity)

        coEvery { rankDao.get(rankId) } returns startRankEntity
        mockNoteRepo.clearConnection(rankDao, noteId, rankId)
        assertEquals(finishRankEntity, startRankEntity)

        coVerifySequence {
            rankDao.get(rankId)

            rankDao.get(rankId)
            rankDao.update(finishRankEntity)
        }
    }

}