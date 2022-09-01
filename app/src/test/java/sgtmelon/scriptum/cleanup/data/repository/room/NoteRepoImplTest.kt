package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.data.room.extension.fromRoom
import sgtmelon.scriptum.cleanup.data.room.extension.safeDelete
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.getText
import sgtmelon.scriptum.cleanup.extension.move
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Test for [NoteRepoImpl].
 */
@Suppress("UnusedEquals")
@ExperimentalCoroutinesApi
class NoteRepoImplTest : ParentRepoTest() {

    private val noteConverter = mockk<NoteConverter>()
    private val rollConverter = mockk<RollConverter>()

    private val noteRepo by lazy { NoteRepoImpl(roomProvider, noteConverter, rollConverter) }
    private val spyNoteRepo by lazy { spyk(noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteConverter, rollConverter)
    }

    // Repo get count and list functions

    @Test fun getCount() = startCoTest {
        TODO()

        val firstIdList = mockk<List<Long>>()
        val secondIdList = mockk<List<Long>>()
        val firstCount = Random.nextInt()
        val secondCount = Random.nextInt()

        coEvery { rankDao.getIdList() } returns firstIdList
        coEvery {
            noteDao.getRankVisibleCount(
                isBin = true,
                rankIdList = firstIdList
            )
        } returns firstCount
        assertEquals(firstCount, noteRepo.getCount(isBin = true))

        coEvery { rankDao.getIdVisibleList() } returns secondIdList
        coEvery {
            noteDao.getRankVisibleCount(
                isBin = false,
                rankIdList = secondIdList
            )
        } returns secondCount
        assertEquals(secondCount, noteRepo.getCount(isBin = false))

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getIdList()
            noteDao.getRankVisibleCount(isBin = true, rankIdList = firstIdList)

            roomProvider.openRoom()
            rankDao.getIdVisibleList()
            noteDao.getRankVisibleCount(isBin = false, rankIdList = secondIdList)
        }
    }

    @Test fun getList() = startCoTest {
        val sort = mockk<Sort>()
        val isBin = Random.nextBoolean()
        val isOptimal = Random.nextBoolean()

        val size = getRandomSize()
        val entityList = List<NoteEntity>(size) { mockk() }
        val itemList = MutableList<NoteItem>(size) { mockk() }

        coEvery { spyNoteRepo.getSortBy(isBin, sort, noteDao) } returns entityList
        coEvery { spyNoteRepo.filterVisible(entityList, rankDao) } returns entityList

        for ((i, entity) in entityList.withIndex()) {
            coEvery {
                spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb)
            } returns itemList[i]
        }

        coEvery { spyNoteRepo.correctRankSort(itemList, sort) } returns itemList

        assertEquals(itemList, spyNoteRepo.getList(sort, isBin, isOptimal, filterVisible = false))
        assertEquals(itemList, spyNoteRepo.getList(sort, isBin, isOptimal, filterVisible = true))

        /**
         * Can't create verify for [IRoomWork.fromRoom] func.
         */
        coVerifyOrder {
            spyNoteRepo.getList(sort, isBin, isOptimal, filterVisible = false)
            spyNoteRepo.roomProvider
            roomProvider.openRoom()
            spyNoteRepo.getSortBy(isBin, sort, noteDao)
            for (entity in entityList) {
                spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb)
            }
            spyNoteRepo.correctRankSort(itemList, sort)

            spyNoteRepo.getList(sort, isBin, isOptimal, filterVisible = true)
            spyNoteRepo.roomProvider
            roomProvider.openRoom()
            spyNoteRepo.getSortBy(isBin, sort, noteDao)
            spyNoteRepo.filterVisible(entityList, rankDao)
            for (entity in entityList) {
                spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb)
            }
            spyNoteRepo.correctRankSort(itemList, sort)
        }
    }

    @Test fun getSortBy() = startCoTest {
        val isBin = Random.nextBoolean()
        val changeList = mockk<MutableList<NoteEntity>>()
        val createList = mockk<MutableList<NoteEntity>>()
        val rankList = mockk<MutableList<NoteEntity>>()
        val colorList = mockk<MutableList<NoteEntity>>()

        coEvery { noteDao.getListByChange(isBin) } returns changeList
        assertEquals(changeList, noteRepo.getSortBy(isBin, Sort.CHANGE, noteDao))

        coEvery { noteDao.getListByCreate(isBin) } returns createList
        assertEquals(createList, noteRepo.getSortBy(isBin, Sort.CREATE, noteDao))

        coEvery { noteDao.getListByRank(isBin) } returns rankList
        assertEquals(rankList, noteRepo.getSortBy(isBin, Sort.RANK, noteDao))

        coEvery { noteDao.getListByColor(isBin) } returns colorList
        assertEquals(colorList, noteRepo.getSortBy(isBin, Sort.COLOR, noteDao))

        coVerifySequence {
            noteDao.getListByChange(isBin)
            noteDao.getListByCreate(isBin)
            noteDao.getListByRank(isBin)
            noteDao.getListByColor(isBin)
        }
    }

    @Test fun filterVisible() = startCoTest {
        val size = getRandomSize()
        val entityList = List<NoteEntity>(size) { mockk() }
        val itemList = List<NoteItem>(size) { mockk() }

        val idList = mockk<List<Long>>()
        val isVisibleList = List(size) { Random.nextBoolean() }

        coEvery { rankDao.getIdVisibleList() } returns idList
        for ((i, entity) in entityList.withIndex()) {
            every { noteConverter.toItem(entity) } returns itemList[i]
            every { itemList[i].isRankVisible(idList) } returns isVisibleList[i]
        }

        val resultList = entityList.filterIndexed { i, _ -> isVisibleList[i] }

        assertEquals(resultList, noteRepo.filterVisible(entityList, rankDao))

        coVerifySequence {
            rankDao.getIdVisibleList()
            for ((i, entity) in entityList.withIndex()) {
                noteConverter.toItem(entity)
                itemList[i].isRankVisible(idList)
            }
        }
    }

    @Test fun correctRankSort() {
        val startList = TestData.Note.itemList
        val finishList = TestData.Note.itemList.apply { move(from = 0) }
        val simpleList: MutableList<NoteItem> = MutableList(size = 5) {
            TestData.Note.firstNote.deepCopy(id = Random.nextLong())
        }

        assertNotEquals(finishList, noteRepo.correctRankSort(startList, Sort.COLOR))
        assertEquals(finishList, noteRepo.correctRankSort(startList, Sort.RANK))
        assertEquals(simpleList, noteRepo.correctRankSort(simpleList, Sort.RANK))
    }

    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()
        val isOptimal = Random.nextBoolean()
        val entity = mockk<NoteEntity>()
        val item = mockk<NoteItem>()

        coEvery { noteDao.get(id) } returns null
        assertNull(noteRepo.getItem(id, isOptimal))

        coEvery { noteDao.get(id) } returns entity
        coEvery { spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb) } returns item
        assertEquals(item, spyNoteRepo.getItem(id, isOptimal))

        /**
         * Can't create verify for [IRoomWork.fromRoom] func.
         */
        coVerifyOrder {
            roomProvider.openRoom()
            noteDao.get(id)

            spyNoteRepo.getItem(id, isOptimal)
            spyNoteRepo.roomProvider
            roomProvider.openRoom()
            noteDao.get(id)
            spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb)
        }
    }

    @Test fun transformNoteEntity() = startCoTest {
        val entity = mockk<NoteEntity>()
        val id = Random.nextLong()
        val isOptimal = Random.nextBoolean()

        val isVisible = Random.nextBoolean()
        val previewList = mockk<MutableList<RollEntity>>()
        val rollList = mockk<MutableList<RollItem>>()
        val alarmEntity = mockk<AlarmEntity>()
        val item = mockk<NoteItem>()

        every { entity.id } returns id
        coEvery { rollVisibleDao.getVisible(id) } returns isVisible
        coEvery { spyNoteRepo.getPreview(id, isVisible, isOptimal, roomDb) } returns previewList
        every { rollConverter.toItem(previewList) } returns rollList
        coEvery { alarmDao.get(id) } returns alarmEntity
        every { noteConverter.toItem(entity, isVisible, rollList, alarmEntity) } returns item

        assertEquals(item, spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb))

        coVerifySequence {
            spyNoteRepo.transformNoteEntity(entity, isOptimal, roomDb)

            entity.id
            rollVisibleDao.getVisible(id)
            entity.id
            spyNoteRepo.getPreview(id, isVisible, isOptimal, roomDb)
            rollConverter.toItem(previewList)
            entity.id
            alarmDao.get(id)
            noteConverter.toItem(entity, isVisible, rollList, alarmEntity)
        }
    }

    @Test fun getPreview() = startCoTest {
        val id = Random.nextLong()

        val firstList = mockk<MutableList<RollEntity>>()
        val secondList = mockk<MutableList<RollEntity>>()
        val thirdList = mockk<MutableList<RollEntity>>()

        coEvery { rollDao.getList(id) } returns firstList
        assertEquals(firstList, noteRepo.getPreview(
            id, isVisible = Random.nextBoolean(), isOptimal = false, db = roomDb
        ))

        coEvery { rollDao.getPreviewList(id) } returns secondList
        assertEquals(secondList, noteRepo.getPreview(
            id, isVisible = null, isOptimal = true, db = roomDb
        ))

        assertEquals(secondList, noteRepo.getPreview(
            id, isVisible = true, isOptimal = true, db = roomDb
        ))

        coEvery { rollDao.getPreviewHideList(id) } returns thirdList
        every { thirdList.isEmpty() } returns false
        assertEquals(thirdList, noteRepo.getPreview(
            id, isVisible = false, isOptimal = true, db = roomDb
        ))

        coEvery { rollDao.getPreviewHideList(id) } returns thirdList
        every { thirdList.isEmpty() } returns true
        assertEquals(secondList, noteRepo.getPreview(
            id, isVisible = false, isOptimal = true, db = roomDb
        ))

        coVerifySequence {
            rollDao.getList(id)
            rollDao.getPreviewList(id)
            rollDao.getPreviewList(id)
            rollDao.getPreviewHideList(id)
            thirdList.isEmpty()
            thirdList == thirdList
            rollDao.getPreviewHideList(id)
            thirdList.isEmpty()
            rollDao.getPreviewList(id)
        }
    }

    @Test fun getRollList() = startCoTest {
        val noteId = Random.nextLong()
        val entityList = mockk<MutableList<RollEntity>>()
        val itemList = mockk<MutableList<RollItem>>()

        coEvery { rollDao.getList(noteId) } returns entityList
        every { rollConverter.toItem(entityList) } returns itemList

        assertEquals(itemList, noteRepo.getRollList(noteId))

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.getList(noteId)
            rollConverter.toItem(entityList)
        }
    }

    // Repo other functions

    @Test fun isListHide() = startCoTest {
        val size = getRandomSize()
        val idList = List(size) { Random.nextLong() }
        val entityList = MutableList<NoteEntity>(size) { mockk() }
        val itemList = MutableList<NoteItem>(size) { mockk() }
        val isVisibleList = List(size) { Random.nextBoolean() }

        coEvery { rankDao.getIdVisibleList() } returns idList
        coEvery { noteDao.getList(false) } returns entityList

        for ((i, entity) in entityList.withIndex()) {
            every { noteConverter.toItem(entity) } returns itemList[i]
            every { itemList[i].isRankVisible(idList) } returns isVisibleList[i]
        }

        assertEquals(isVisibleList.any { !it }, noteRepo.isListHide())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getIdVisibleList()
            noteDao.getList(false)

            for ((i, entity) in entityList.withIndex()) {
                noteConverter.toItem(entity)
                itemList[i].isRankVisible(idList)

                if (!isVisibleList[i]) break
            }
        }
    }

    // Repo work with delete functions

    @Test fun clearBin() = startCoTest {
        fun indexToId(i: Int) = (i * i).toLong()

        val size = getRandomSize()
        val itemList = List<NoteEntity>(size) {
            mockk {
                every { id } returns it.toLong()
                every { rankId } returns indexToId(it)
            }
        }

        coEvery { noteDao.getList(true) } returns itemList
        coEvery { spyNoteRepo.clearConnection(any(), any(), rankDao) } returns Unit

        spyNoteRepo.clearBin()

        coVerifySequence {
            spyNoteRepo.clearBin()
            spyNoteRepo.roomProvider

            roomProvider.openRoom()
            noteDao.getList(true)
            for ((i, item) in itemList.withIndex()) {
                item.id
                item.rankId
                spyNoteRepo.clearConnection(i.toLong(), indexToId(i), rankDao)
            }
            noteDao.delete(itemList)
        }
    }

    @Test fun deleteNote() = startCoTest {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()
        val deleteItem = mockk<NoteItem>()
        val entity = mockk<NoteEntity>()

        every { item.id } returns id
        every { item.onDelete() } returns deleteItem
        every { noteConverter.toEntity(deleteItem) } returns entity

        noteRepo.deleteNote(item)

        coVerifySequence {
            roomProvider.openRoom()
            item.id
            alarmDao.delete(id)
            item.onDelete()
            noteConverter.toEntity(deleteItem)
            noteDao.update(entity)
        }
    }

    @Test fun restoreNote() = startCoTest {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()
        val restoreItem = mockk<NoteItem>()
        val entity = mockk<NoteEntity>()

        every { item.id } returns id
        every { item.onRestore() } returns restoreItem
        every { noteConverter.toEntity(restoreItem) } returns entity

        noteRepo.restoreNote(item)

        coVerifySequence {
            roomProvider.openRoom()
            item.onRestore()
            noteConverter.toEntity(restoreItem)
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
        coEvery { spyNoteRepo.clearConnection(id, rankId, rankDao) } returns Unit
        every { noteConverter.toEntity(item) } returns entity

        spyNoteRepo.clearNote(item)

        coVerifySequence {
            spyNoteRepo.clearNote(item)
            spyNoteRepo.roomProvider

            roomProvider.openRoom()
            item.id
            item.rankId
            spyNoteRepo.clearConnection(id, rankId, rankDao)
            noteConverter.toEntity(item)
            noteDao.delete(entity)
        }
    }

    @Test fun clearConnection() = startCoTest {
        val noteId = Random.nextLong()
        val rankId = Random.nextLong()
        val entity = mockk<RankEntity>()
        val noteIdList = mockk<MutableList<Long>>()

        coEvery { rankDao.get(rankId) } returns null
        noteRepo.clearConnection(noteId, rankId, rankDao)

        coEvery { rankDao.get(rankId) } returns entity
        every { entity.noteId } returns noteIdList
        every { noteIdList.remove(noteId) } returns Random.nextBoolean()
        noteRepo.clearConnection(noteId, rankId, rankDao)

        coVerifySequence {
            rankDao.get(rankId)

            rankDao.get(rankId)
            entity.noteId
            noteIdList.remove(noteId)
            rankDao.update(entity)
        }
    }

    // Repo save and update functions

    @Test fun `convertNote from text to roll`() = startCoTest {
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
        for (it in rollItemList) {
            every { it.id = rollId } returns Unit
        }

        every { noteConverter.toEntity(finishItem) } returns finishEntity

        assertEquals(finishItem, noteRepo.convertNote(startItem))

        coVerifySequence {
            roomProvider.openRoom()
            startItem.onConvert()
            finishItem.list
            for (it in rollItemList) {
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

    @Test fun `convertNote from roll to text`() = startCoTest {
        val startItem = mockk<NoteItem.Roll>()
        val finishItem = mockk<NoteItem.Text>()
        val finishEntity = mockk<NoteEntity>()

        val id = Random.nextLong()
        val entityList = mockk<MutableList<RollEntity>>()
        val itemList = mockk<MutableList<RollItem>>()

        every { startItem.onConvert() } returns finishItem
        every { noteConverter.toEntity(finishItem) } returns finishEntity
        every { finishItem.id } returns id
        assertEquals(finishItem, noteRepo.convertNote(startItem, useCache = true))

        every { startItem.id } returns id
        coEvery { rollDao.getList(id) } returns entityList
        every { rollConverter.toItem(entityList) } returns itemList
        every { startItem.onConvert(itemList) } returns finishItem
        assertEquals(finishItem, noteRepo.convertNote(startItem, useCache = false))

        coVerifySequence {
            roomProvider.openRoom()
            startItem.onConvert()
            noteConverter.toEntity(finishItem)
            noteDao.update(finishEntity)
            finishItem.id
            rollDao.delete(id)
            finishItem == finishItem

            roomProvider.openRoom()
            startItem.id
            rollDao.getList(id)
            rollConverter.toItem(entityList)
            startItem.onConvert(itemList)
            noteConverter.toEntity(finishItem)
            noteDao.update(finishEntity)
            finishItem.id
            rollDao.delete(id)
            finishItem == finishItem
        }
    }

    @Test fun `getCopyText for text note`() = startCoTest {
        val item = mockk<NoteItem.Text>()

        val name = nextString()
        val text = nextString()

        every { item.name } returns ""
        every { item.text } returns text
        assertEquals(text, noteRepo.getCopyText(item))

        every { item.name } returns name
        assertEquals("$name\n$text", noteRepo.getCopyText(item))

        verifySequence {
            item.name
            item.text

            item.name
            item.name
            item.text
        }
    }

    @Test fun `getCopyText for roll note`() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        val id = Random.nextLong()
        val name = nextString()
        val list = mockk<MutableList<RollItem>>()
        val text = nextString()

        FastMock.listExtension()

        every { item.name } returns ""
        every { item.id } returns id
        coEvery { spyNoteRepo.getRollList(id) } returns list
        every { list.getText() } returns text
        assertEquals(text, spyNoteRepo.getCopyText(item))

        every { item.name } returns name
        assertEquals("$name\n$text", spyNoteRepo.getCopyText(item))

        coVerifySequence {
            spyNoteRepo.getCopyText(item)
            item.name
            item.id
            spyNoteRepo.getRollList(id)
            list.getText()

            spyNoteRepo.getCopyText(item)
            item.name
            item.name
            item.id
            spyNoteRepo.getRollList(id)
            list.getText()
        }
    }

    @Test fun `saveNote text`() = startCoTest {
        val item = mockk<NoteItem.Text>()
        val entity = mockk<NoteEntity>()
        val id = Random.nextLong()

        every { noteConverter.toEntity(item) } returns entity
        noteRepo.saveNote(item, isCreate = false)

        coEvery { noteDao.insert(entity) } returns id
        every { item.id = id } returns Unit
        noteRepo.saveNote(item, isCreate = true)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.update(entity)

            roomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.insert(entity)
            item.id = id
        }
    }

    @Test fun `saveNote roll onCreate`() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val entity = mockk<NoteEntity>()

        val id = Random.nextLong()
        val size = getRandomSize()
        val itemList = MutableList<RollItem>(size) { mockk() }
        val entityList = MutableList<RollEntity>(size) { mockk() }
        val idList = List(size) { Random.nextLong() }

        every { noteConverter.toEntity(item) } returns entity
        coEvery { noteDao.insert(entity) } returns id
        every { item.id = id } returns Unit
        every { item.id } returns id
        every { item.list } returns itemList

        for ((i, rollItem) in itemList.withIndex()) {
            every { rollConverter.toEntity(id, rollItem) } returns entityList[i]
            coEvery { rollDao.insert(entityList[i]) } returns idList[i]
            every { rollItem.id = idList[i] } returns Unit
        }

        noteRepo.saveNote(item, isCreate = true)

        /**
         * Can't create verify sequence because of list items (equals checks in log).
         */
        coVerifyOrder {
            roomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.insert(entity)
            item.id = id
            item.list

            for ((i, rollItem) in itemList.withIndex()) {
                item.id
                rollConverter.toEntity(id, rollItem)
                rollDao.insert(entityList[i])
                rollItem.id = idList[i]
            }
        }
    }

    @Test fun `saveNote roll onUpdate`() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val entity = mockk<NoteEntity>()

        val id = Random.nextLong()
        val size = getRandomSize()
        val itemList = MutableList<RollItem>(size) { mockk() }
        val entityList = MutableList<RollEntity>(size) { mockk() }
        val idList = List(size) { Random.nextLong() }
        val positionList = List(size) { Random.nextInt() }
        val textList = List(size) { nextString() }

        every { noteConverter.toEntity(item) } returns entity
        every { item.id } returns id
        every { item.list } returns itemList

        val idSaveList = arrayListOf<Long>()
        fun indexToId(i: Int) = (i * i).toLong()

        for ((i, rollItem) in itemList.withIndex()) {
            if (i.isDivideEntirely()) {
                every { rollItem.id } returns null
                every { rollConverter.toEntity(id, rollItem) } returns entityList[i]
                coEvery { rollDao.insert(entityList[i]) } returns idList[i]
                every { rollItem.id = idList[i] } returns Unit
            } else {
                val itemId = indexToId(i)
                idSaveList.add(itemId)

                every { rollItem.id } returns itemId
                every { rollItem.position } returns positionList[i]
                every { rollItem.text } returns textList[i]
            }
        }

        FastMock.daoExtension()
        coEvery { rollDao.safeDelete(id, idSaveList) } returns Unit

        noteRepo.saveNote(item, isCreate = false)

        /**
         * Can't create verify sequence because of list items (equals checks in log).
         */
        coVerify {
            roomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.update(entity)
            item.list

            for ((i, rollItem) in itemList.withIndex()) {
                rollItem.id

                if (i.isDivideEntirely()) {
                    item.id
                    rollConverter.toEntity(id, rollItem)
                    rollDao.insert(entityList[i])
                    rollItem.id = idList[i]
                } else {
                    item.id
                    rollItem.position
                    rollItem.text
                    rollDao.update(indexToId(i), positionList[i], textList[i])
                }

                rollItem.id
            }

            item.id
            rollDao.safeDelete(id, idSaveList)
        }
    }

    @Test fun `updateRollCheck for item`() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val p = (0..10).random()
        val list = mockk<MutableList<RollItem>>()
        val rollItem = mockk<RollItem>()
        val rollId = Random.nextLong()
        val isCheck = Random.nextBoolean()
        val entity = mockk<NoteEntity>()

        every { item.list } returns list
        every { list.size } returns 0
        noteRepo.updateRollCheck(item, p)

        every { list.size } returns 11
        every { list[p] } returns rollItem
        every { rollItem.id } returns null
        noteRepo.updateRollCheck(item, p)

        every { rollItem.id } returns rollId
        every { rollItem.isCheck } returns isCheck
        every { noteConverter.toEntity(item) } returns entity
        noteRepo.updateRollCheck(item, p)

        coVerifySequence {
            roomProvider.openRoom()
            item.list
            list.size

            roomProvider.openRoom()
            item.list
            list.size
            list[p]
            rollItem.id

            roomProvider.openRoom()
            item.list
            list.size
            list[p]
            rollItem.id
            rollItem.isCheck
            rollDao.updateCheck(rollId, isCheck)
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }

    @Test fun `updateRollCheck for list`() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val id = Random.nextLong()
        val entity = mockk<NoteEntity>()

        val isCheck = Random.nextBoolean()

        every { item.id } returns id
        every { noteConverter.toEntity(item) } returns entity

        noteRepo.updateRollCheck(item, isCheck)

        coVerifySequence {
            roomProvider.openRoom()
            item.id
            rollDao.updateAllCheck(id, isCheck)
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }

    @Test fun updateNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val entity = mockk<NoteEntity>()

        every { noteConverter.toEntity(item) } returns entity

        noteRepo.updateNote(item)

        coVerifySequence {
            roomProvider.openRoom()
            noteConverter.toEntity(item)
            noteDao.update(entity)
        }
    }

    @Test fun setRollVisible() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val id = Random.nextLong()
        val isVisible = Random.nextBoolean()
        val entity = RollVisibleEntity(noteId = id, value = !isVisible)

        every { item.id } returns id
        coEvery { rollVisibleDao.getVisible(id) } returns null
        every { item.isVisible } returns !isVisible
        coEvery { rollVisibleDao.insert(entity) } returns Random.nextLong()
        noteRepo.setRollVisible(item)

        coEvery { rollVisibleDao.getVisible(id) } returns isVisible
        noteRepo.setRollVisible(item)

        every { item.isVisible } returns isVisible
        noteRepo.setRollVisible(item)

        coVerifySequence {
            roomProvider.openRoom()
            item.id
            rollVisibleDao.getVisible(id)
            item.id
            item.isVisible
            rollVisibleDao.insert(entity)

            roomProvider.openRoom()
            item.id
            rollVisibleDao.getVisible(id)
            item.isVisible
            item.id
            item.isVisible
            rollVisibleDao.update(id, !isVisible)

            roomProvider.openRoom()
            item.id
            rollVisibleDao.getVisible(id)
            item.isVisible
        }
    }

    // Repo backup functions

    @Test fun getNoteBackup() = startCoTest {
        val noteList = mockk<List<NoteEntity>>()

        coEvery { noteDao.getList(isBin = false) } returns noteList

        assertEquals(noteList, noteRepo.getNoteBackupList())

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.getList(isBin = false)
        }
    }

    @Test fun getRollBackup() = startCoTest {
        val rollList = mockk<List<RollEntity>>()
        val noteIdList = mockk<List<Long>>()

        coEvery { rollDao.getList(noteIdList) } returns rollList

        assertEquals(rollList, noteRepo.getRollBackupList(noteIdList))

        coVerifySequence {
            roomProvider.openRoom()
            rollDao.getList(noteIdList)
        }
    }

    @Test fun getRollVisibleBackup() = startCoTest {
        val rollVisibleList = mockk<List<RollVisibleEntity>>()
        val noteIdList = mockk<List<Long>>()

        coEvery { rollVisibleDao.getList(noteIdList) } returns rollVisibleList

        assertEquals(rollVisibleList, noteRepo.getRollVisibleBackupList(noteIdList))

        coVerifySequence {
            roomProvider.openRoom()
            rollVisibleDao.getList(noteIdList)
        }
    }
}