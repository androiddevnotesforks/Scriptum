package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
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

    private val badNoteRepo by lazy { NoteRepo(badRoomProvider, noteConverter, rollConverter) }
    private val goodNoteRepo by lazy { NoteRepo(goodRoomProvider, noteConverter, rollConverter) }
    private val goodNoteRepo2 by lazy { NoteRepo(goodRoomProvider, NoteConverter(), RollConverter()) }

    @Test fun getCount() = startCoTest {
        val notesCount = Random.nextInt()
        val binCount = Random.nextInt()

        val rankIdVisibleList = List(size = 2) { Random.nextLong() }
        val rankIdList = List(size = 5) { Random.nextLong() }

        assertNull(badNoteRepo.getCount(bin = false))
        assertNull(badNoteRepo.getCount(bin = true))

        coEvery { rankDao.getIdVisibleList() } returns rankIdVisibleList
        coEvery { noteDao.getCount(bin = false, rankIdList = rankIdVisibleList) } returns notesCount
        assertEquals(notesCount, goodNoteRepo.getCount(bin = false))

        coEvery { rankDao.getIdList() } returns rankIdList
        coEvery { noteDao.getCount(bin = true, rankIdList = rankIdList) } returns binCount
        assertEquals(binCount, goodNoteRepo.getCount(bin = true))

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.getIdVisibleList()
            noteDao.getCount(bin = false, rankIdList = rankIdVisibleList)

            goodRoomProvider.openRoom()
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

        goodNoteRepo.correctRankSort(finishList, Sort.RANK)

        assertNull(badNoteRepo.getList(
                sort, bin, isOptimal = Random.nextBoolean(), filterVisible = Random.nextBoolean()
        ))

        coEvery { noteDao.getByRank(bin) } returns entityList
        coEvery { rankDao.getIdVisibleList() } returns listOf()
        coEvery {
            with(rollDao) { if (isOptimal) getView(any()) else get(any()) }
        } returns mutableListOf()
        coEvery { alarmDao.get(noteId = any()) } returns null

        assertEquals(finishList, goodNoteRepo2.getList(sort, bin, isOptimal, filterVisible = false))
        assertEquals(
                finishFilterList, goodNoteRepo2.getList(sort, bin, isOptimal, filterVisible = true)
        )

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.getByRank(bin)
            entityList.forEach {
                if (isOptimal) rollDao.getView(it.id) else rollDao.get(it.id)
                alarmDao.get(it.id)
            }

            goodRoomProvider.openRoom()
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

        assertNull(goodNoteRepo.getSortBy(noteDao, sort = 10, bin = false))
        assertNull(goodNoteRepo.getSortBy(noteDao, sort = 10, bin = true))

        coEvery { noteDao.getByChange(any()) } returns changeList
        assertEquals(changeList, goodNoteRepo.getSortBy(noteDao, Sort.CHANGE, bin = false))
        assertEquals(changeList, goodNoteRepo.getSortBy(noteDao, Sort.CHANGE, bin = true))

        coEvery { noteDao.getByCreate(any()) } returns createList
        assertEquals(createList, goodNoteRepo.getSortBy(noteDao, Sort.CREATE, bin = false))
        assertEquals(createList, goodNoteRepo.getSortBy(noteDao, Sort.CREATE, bin = true))

        coEvery { noteDao.getByRank(any()) } returns rankList
        assertEquals(rankList, goodNoteRepo.getSortBy(noteDao, Sort.RANK, bin = false))
        assertEquals(rankList, goodNoteRepo.getSortBy(noteDao, Sort.RANK, bin = true))

        coEvery { noteDao.getByColor(any()) } returns colorList
        assertEquals(colorList, goodNoteRepo.getSortBy(noteDao, Sort.COLOR, bin = false))
        assertEquals(colorList, goodNoteRepo.getSortBy(noteDao, Sort.COLOR, bin = true))

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

        assertEquals(entityList.subList(0, 3), goodNoteRepo.filterVisible(rankDao, entityList))

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

        assertNotEquals(finishList, goodNoteRepo.correctRankSort(startList, Sort.COLOR))
        assertEquals(finishList, goodNoteRepo.correctRankSort(startList, Sort.RANK))
        assertEquals(simpleList, goodNoteRepo.correctRankSort(simpleList, Sort.RANK))
    }


    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()

        val noteItem = TestData.Note.firstNote
        val alarmEntity = AlarmEntity(id = noteItem.alarmId, date = noteItem.alarmDate)

        val noteEntity = NoteConverter().toEntity(noteItem)

        assertNull(badNoteRepo.getItem(id, isOptimal = false))
        assertNull(badNoteRepo.getItem(id, isOptimal = true))

        coEvery { noteDao.get(id) } returns null
        assertNull(goodNoteRepo.getItem(id, isOptimal = Random.nextBoolean()))

        every { rollConverter.toItem(mutableListOf()) } returns mutableListOf()
        every { noteConverter.toItem(noteEntity, mutableListOf(), alarmEntity) } returns noteItem

        coEvery { noteDao.get(id) } returns noteEntity
        coEvery { rollDao.get(id) } returns mutableListOf()
        coEvery { rollDao.getView(id) } returns mutableListOf()
        coEvery { alarmDao.get(id) } returns alarmEntity
        assertEquals(noteItem, goodNoteRepo.getItem(id, isOptimal = false))
        assertEquals(noteItem, goodNoteRepo.getItem(id, isOptimal = true))

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.get(id)

            goodRoomProvider.openRoom()
            noteDao.get(id)
            rollDao.get(id)
            rollConverter.toItem(mutableListOf())
            alarmDao.get(id)
            noteConverter.toItem(noteEntity, mutableListOf(), alarmEntity)

            goodRoomProvider.openRoom()
            noteDao.get(id)
            rollDao.getView(id)
            rollConverter.toItem(mutableListOf())
            alarmDao.get(id)
            noteConverter.toItem(noteEntity, mutableListOf(), alarmEntity)
        }
    }

    @Test fun getPreview() = startCoTest {
        val id = Random.nextLong()

        val firstList = MutableList(size = 5) { RollEntity(id = Random.nextLong()) }
        val secondList = MutableList(size = 5) { RollEntity(id = Random.nextLong()) }

        coEvery { rollDao.get(id) } returns firstList
        assertEquals(firstList, goodNoteRepo.getPreview(rollDao, id, isOptimal = false))

        coEvery { rollDao.getView(id) } returns secondList
        assertEquals(secondList, goodNoteRepo.getPreview(rollDao, id, isOptimal = true))

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

        assertNull(badNoteRepo.getRollList(noteId))
        assertEquals(itemList, goodNoteRepo.getRollList(noteId))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rollDao.get(noteId)
            rollConverter.toItem(entityList)
        }
    }


    @Test fun isListHide() = startCoTest {
        val idList = listOf<Long>(1, 2, 3)

        coEvery { rankDao.getIdVisibleList() } returns idList

        assertNull(badNoteRepo.isListHide())

        val entityList = mutableListOf<NoteEntity>()

        var entity = NoteEntity()
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, goodNoteRepo.isListHide())

        entity = NoteEntity(rankId = 0)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, goodNoteRepo.isListHide())

        entity = NoteEntity(rankPs = 0)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, goodNoteRepo.isListHide())

        entity = NoteEntity(rankId = 0, rankPs = 0)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(true, goodNoteRepo.isListHide())

        entity = NoteEntity(rankId = 1, rankPs = 1)
        entityList.add(entity)

        every { noteConverter.toItem(entity) } returns NoteConverter().toItem(entity)
        coEvery { noteDao.get(false) } returns listOf(entity)
        assertEquals(false, goodNoteRepo.isListHide())

        coVerifySequence {
            badRoomProvider.openRoom()

            entityList.forEach {
                goodRoomProvider.openRoom()
                noteDao.get(false)
                noteConverter.toItem(it)
                rankDao.getIdVisibleList()
            }
        }
    }

    @Test fun clearBin() = startCoTest {
        val itemList = NoteConverter().toEntity(TestData.Note.itemList)

        itemList.forEach { coEvery { rankDao.get(it.rankId) } returns null }
        coEvery { noteDao.get(true) } returns itemList

        badNoteRepo.clearBin()
        goodNoteRepo.clearBin()

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.get(true)
            itemList.forEach {
                rankDao.get(it.rankId)
            }
            noteDao.delete(itemList)
        }
    }


    @Test fun deleteNote() = startCoTest {
        val startItem = TestData.Note.firstNote
        val finalItem = startItem.deepCopy().onDelete()

        val entity = NoteConverter().toEntity(finalItem)

        every { noteConverter.toEntity(finalItem) } returns entity

        badNoteRepo.deleteNote(startItem)
        goodNoteRepo.deleteNote(startItem)

        assertEquals(startItem, finalItem)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
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

        badNoteRepo.restoreNote(startItem)
        goodNoteRepo.restoreNote(startItem)

        assertEquals(startItem, finalItem)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteConverter.toEntity(finalItem)
            noteDao.update(entity)
        }
    }

    @Test fun clearNote() = startCoTest {
        val item = TestData.Note.itemList.random()
        val entity = NoteConverter().toEntity(item)

        every { noteConverter.toEntity(item) } returns entity
        coEvery { rankDao.get(item.rankId) } returns null

        badNoteRepo.clearNote(item)
        goodNoteRepo.clearNote(item)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.get(item.rankId)
            noteConverter.toEntity(item)
            noteDao.delete(entity)
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

        assertNull(badNoteRepo.convertNote(startItem))
        assertEquals(finishItem, goodNoteRepo.convertNote(startItem))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
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

        assertNull(badNoteRepo.convertNote(rollItem, useCache = false))
        assertNull(badNoteRepo.convertNote(rollItem, useCache = true))

        assertEquals(textItem, goodNoteRepo.convertNote(rollItem, useCache = false))
        assertEquals(textItem, goodNoteRepo.convertNote(rollItem, useCache = true))

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rollDao.get(rollItem.id)
            rollConverter.toItem(entityList)
            noteConverter.toEntity(textItem)
            noteDao.update(textEntity)
            rollDao.delete(rollItem.id)

            goodRoomProvider.openRoom()
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

        assertEquals(firstText, badNoteRepo.getCopyText(firstItem))
        assertEquals(secondText, badNoteRepo.getCopyText(secondItem))

        assertEquals(firstText, goodNoteRepo.getCopyText(firstItem))
        assertEquals(secondText, goodNoteRepo.getCopyText(secondItem))
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

        assertEquals("", badNoteRepo.getCopyText(firstItem))
        assertEquals("${secondItem.name}\n", badNoteRepo.getCopyText(secondItem))

        every { rollConverter.toItem(entityList) } returns itemList
        coEvery { rollDao.get(any()) } returns entityList

        assertEquals(firstText, goodNoteRepo.getCopyText(firstItem))
        assertEquals(secondText, goodNoteRepo.getCopyText(secondItem))

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rollDao.get(firstItem.id)
            rollConverter.toItem(entityList)

            goodRoomProvider.openRoom()
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

        badNoteRepo.saveNote(startItem, isCreate = false)
        badNoteRepo.saveNote(startItem, isCreate = true)

        goodNoteRepo.saveNote(startItem, isCreate = false)
        assertNotEquals(startItem, finishItem)

        goodNoteRepo.saveNote(startItem, isCreate = true)
        assertEquals(startItem, finishItem)

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteConverter.toEntity(startItem)
            noteDao.update(entity)

            goodRoomProvider.openRoom()
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

        badNoteRepo.saveNote(startItem, isCreate = true)
        assertNotEquals(startItem, finishItem)

        goodNoteRepo.saveNote(startItem, isCreate = true)
        assertEquals(startItem.list, finishItem.list)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
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

        badNoteRepo.saveNote(startItem, isCreate = false)
        assertNotEquals(startItem.list, finishItem.list)

        goodNoteRepo.saveNote(startItem, isCreate = false)
        assertEquals(startItem, finishItem)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
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

        badNoteRepo.updateRollCheck(noteItem, p)
        goodNoteRepo.updateRollCheck(noteItem, Random.nextInt())

        rollItem.id = null
        goodNoteRepo.updateRollCheck(noteItem, p)

        rollItem.id = rollId
        goodNoteRepo.updateRollCheck(noteItem, p)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            goodRoomProvider.openRoom()

            goodRoomProvider.openRoom()
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

        badNoteRepo.updateRollCheck(item, check)
        goodNoteRepo.updateRollCheck(item, check)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rollDao.updateAllCheck(item.id, check)
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }

    @Test fun updateNote() = startCoTest {
        val item = TestData.Note.itemList.random()
        val entity = NoteConverter().toEntity(item)

        every { noteConverter.toEntity(item) } returns entity

        badNoteRepo.updateNote(item)
        goodNoteRepo.updateNote(item)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }


    @Test fun setRollVisible() = startCoTest {
        val id = Random.nextLong()
        val entity = RollVisibleEntity(noteId = id, value = false)

        badNoteRepo.setRollVisible(id, isVisible = false)
        badNoteRepo.setRollVisible(id, isVisible = true)

        coEvery { rollVisibleDao.get(id) } returns null
        coEvery { rollVisibleDao.insert(any()) } returns Random.nextLong()
        goodNoteRepo.setRollVisible(id, isVisible = false)

        coEvery { rollVisibleDao.get(id) } returns false
        goodNoteRepo.setRollVisible(id, isVisible = false)
        goodNoteRepo.setRollVisible(id, isVisible = true)

        coVerifySequence {
            badRoomProvider.openRoom()
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rollVisibleDao.get(id)
            rollVisibleDao.insert(entity)

            goodRoomProvider.openRoom()
            rollVisibleDao.get(id)

            goodRoomProvider.openRoom()
            rollVisibleDao.get(id)
            rollVisibleDao.update(id, value = true)
        }
    }

    @Test fun getRollVisible() = startCoTest {
        val id = Random.nextLong()
        val entity = RollVisibleEntity(noteId = id)

        assertNull(badNoteRepo.getRollVisible(id))

        coEvery { rollVisibleDao.get(id) } returns null
        coEvery { rollVisibleDao.insert(entity) } returns Random.nextLong()
        assertEquals(DbData.RollVisible.Default.VALUE, goodNoteRepo.getRollVisible(id))

        coEvery { rollVisibleDao.get(id) } returns false
        assertEquals(false, goodNoteRepo.getRollVisible(id))

        coEvery { rollVisibleDao.get(id) } returns true
        assertEquals(true, goodNoteRepo.getRollVisible(id))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rollVisibleDao.get(id)
            rollVisibleDao.insert(entity)

            goodRoomProvider.openRoom()
            rollVisibleDao.get(id)

            goodRoomProvider.openRoom()
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
        goodNoteRepo.clearConnection(rankDao, noteId, rankId)
        assertNotEquals(finishRankEntity, startRankEntity)

        coEvery { rankDao.get(rankId) } returns startRankEntity
        goodNoteRepo.clearConnection(rankDao, noteId, rankId)
        assertEquals(finishRankEntity, startRankEntity)

        coVerifySequence {
            rankDao.get(rankId)

            rankDao.get(rankId)
            rankDao.update(finishRankEntity)
        }
    }

}