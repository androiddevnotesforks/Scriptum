package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.converter.model.RollConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onConvert
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onDelete
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onRestore
import sgtmelon.test.common.getRandomSize
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString
import kotlin.random.Random

/**
 * Test for [NoteRepoImpl].
 */
@Suppress("UnusedEquals")
class NoteRepoImplTest : ParentRepoTest() {

    private val noteConverter = mockk<NoteConverter>()
    private val rollConverter = mockk<RollConverter>()

    private val repository by lazy {
        NoteRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource, rankDataSource, alarmDataSource,
            noteConverter, rollConverter
        )
    }
    private val spyRepository by lazy { spyk(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteConverter, rollConverter)
    }

    // Repo get count and list functions

    @Test fun getBindNoteList() {
        TODO()
    }

    @Test fun getBinList() {
        TODO()
    }

    @Test fun getNotesList() {
        TODO()
    }

    @Test fun getList() {
        TODO("write different tests getBind, getNotes, getBin lists")
        //        val sort = mockk<Sort>()
        //        val isBin = Random.nextBoolean()
        //        val isOptimal = Random.nextBoolean()
        //
        //        val size = getRandomSize()
        //        val entityList = List<NoteEntity>(size) { mockk() }
        //        val itemList = MutableList<NoteItem>(size) { mockk() }
        //
        //        coEvery { noteDataSource.getList(sort, isBin) } returns entityList
        //        coEvery { spyRepository.filterVisible(entityList) } returns entityList
        //
        //        for ((i, entity) in entityList.withIndex()) {
        //            coEvery { spyRepository.transformNoteEntity(entity, isOptimal) } returns itemList[i]
        //        }
        //
        //        coEvery { spyRepository.correctRankSort(sort, itemList) } returns itemList
        //
        //        runBlocking {
        //            assertEquals(
        //                spyRepository.getList(sort, isBin, isOptimal, filterVisible = false), itemList
        //            )
        //            assertEquals(
        //                spyRepository.getList(sort, isBin, isOptimal, filterVisible = true), itemList
        //            )
        //        }
        //
        //        coVerifySequence {
        //            spyRepository.getList(sort, isBin, isOptimal, filterVisible = false)
        //
        //            noteDataSource.getList(sort, isBin)
        //            for (entity in entityList) {
        //                spyRepository.transformNoteEntity(entity, isOptimal)
        //            }
        //            spyRepository.correctRankSort(sort, itemList)
        //
        //            spyRepository.getList(sort, isBin, isOptimal, filterVisible = true)
        //
        //            noteDataSource.getList(sort, isBin)
        //            spyRepository.filterVisible(entityList)
        //            for (entity in entityList) {
        //                spyRepository.transformNoteEntity(entity, isOptimal)
        //            }
        //            spyRepository.correctRankSort(sort, itemList)
        //        }
    }

    @Test fun filterVisible() {
        TODO()
        //        val size = getRandomSize()
        //        val entityList = List<NoteEntity>(size) { mockk() }
        //        val itemList = List<NoteItem>(size) { mockk() }
        //
        //        val idList = mockk<List<Long>>()
        //        val isVisibleList = List(size) { Random.nextBoolean() }
        //
        //        coEvery { rankDataSource.getIdVisibleList() } returns idList
        //        for ((i, entity) in entityList.withIndex()) {
        //            every { noteConverter.toItem(entity) } returns itemList[i]
        //            every { itemList[i].isRankVisible(idList) } returns isVisibleList[i]
        //        }
        //
        //        val resultList = entityList.filterIndexed { i, _ -> isVisibleList[i] }
        //
        //        runBlocking {
        //            assertEquals(repository.filterVisible(entityList), resultList)
        //        }
        //
        //        coVerifySequence {
        //            rankDataSource.getIdVisibleList()
        //            for ((i, entity) in entityList.withIndex()) {
        //                noteConverter.toItem(entity)
        //                itemList[i].isRankVisible(idList)
        //            }
        //        }
    }

    @Test fun correctRankSort() {
        TODO()
        //        val startList = TestData.Note.itemList
        //        val finishList = TestData.Note.itemList.apply { move(from = 0) }
        //        val simpleList: MutableList<NoteItem> = MutableList(size = 5) {
        //            TestData.Note.firstNote.deepCopy(id = Random.nextLong())
        //        }
        //
        //        assertNotEquals(finishList, repository.correctRankSort(Sort.COLOR, startList))
        //        assertEquals(finishList, repository.correctRankSort(Sort.RANK, startList))
        //        assertEquals(simpleList, repository.correctRankSort(Sort.RANK, simpleList))
    }

    @Test fun getItem() {
        val id = Random.nextLong()
        val entity = mockk<NoteEntity>()
        val item = mockk<NoteItem>()

        coEvery { noteDataSource.get(id) } returns null

        runBlocking {
            assertNull(repository.getItem(id))
        }

        coEvery { noteDataSource.get(id) } returns entity
        coEvery { spyRepository.transformNoteEntity(entity) } returns item

        runBlocking {
            assertEquals(item, spyRepository.getItem(id))
        }

        coVerifyOrder {
            noteDataSource.get(id)

            spyRepository.getItem(id)
            noteDataSource.get(id)
            spyRepository.transformNoteEntity(entity)
        }
    }

    @Test fun transformNoteEntity() {
        val entity = mockk<NoteEntity>()
        val id = Random.nextLong()

        val isVisible = Random.nextBoolean()
        val rollList = mockk<MutableList<RollItem>>()
        val alarmEntity = mockk<AlarmEntity>()
        val item = mockk<NoteItem>()

        every { entity.id } returns id
        coEvery { rollVisibleDataSource.getVisible(id) } returns isVisible
        coEvery { spyRepository.getRollList(id) } returns rollList
        coEvery { alarmDataSource.get(id) } returns alarmEntity
        every { noteConverter.toItem(entity, isVisible, rollList, alarmEntity) } returns item

        runBlocking {
            assertEquals(spyRepository.transformNoteEntity(entity), item)
        }

        coVerifySequence {
            spyRepository.transformNoteEntity(entity)

            entity.id
            rollVisibleDataSource.getVisible(id)
            entity.id
            spyRepository.getRollList(id)
            alarmDataSource.get(id)
            noteConverter.toItem(entity, isVisible, rollList, alarmEntity)
        }
    }

    @Test fun getRollList() {
        val noteId = Random.nextLong()
        val entityList = mockk<List<RollEntity>>()
        val itemList = mockk<MutableList<RollItem>>()

        coEvery { rollDataSource.getList(noteId) } returns entityList
        every { rollConverter.toItem(entityList) } returns itemList

        runBlocking {
            assertEquals(repository.getRollList(noteId), itemList)
        }

        coVerifySequence {
            rollDataSource.getList(noteId)
            rollConverter.toItem(entityList)
        }
    }

    // Repo work with delete functions

    @Test fun clearBin() {
        fun indexToId(i: Int) = (i * i).toLong()

        val size = getRandomSize()
        val itemList = List<NoteEntity>(size) {
            mockk {
                every { id } returns it.toLong()
                every { rankId } returns indexToId(it)
            }
        }

        coEvery { noteDataSource.getList(isBin = true) } returns itemList
        coEvery { spyRepository.clearConnection(any(), any()) } returns Unit

        runBlocking {
            spyRepository.clearBin()
        }

        coVerifySequence {
            spyRepository.clearBin()

            noteDataSource.getList(true)
            for ((i, item) in itemList.withIndex()) {
                item.id
                item.rankId
                spyRepository.clearConnection(i.toLong(), indexToId(i))
            }
            noteDataSource.delete(itemList)
        }
    }

    @Test fun deleteNote() {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()
        val deleteItem = mockk<NoteItem>()
        val entity = mockk<NoteEntity>()

        every { item.id } returns id
        every { item.onDelete() } returns deleteItem
        every { noteConverter.toEntity(deleteItem) } returns entity

        runBlocking {
            repository.deleteNote(item)
        }

        coVerifySequence {
            item.id
            alarmDataSource.delete(id)
            item.onDelete()
            noteConverter.toEntity(deleteItem)
            noteDataSource.update(entity)
        }
    }

    @Test fun restoreNote() {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()
        val restoreItem = mockk<NoteItem>()
        val entity = mockk<NoteEntity>()

        every { item.id } returns id
        every { item.onRestore() } returns restoreItem
        every { noteConverter.toEntity(restoreItem) } returns entity

        runBlocking {
            repository.restoreNote(item)
        }

        coVerifySequence {
            item.onRestore()
            noteConverter.toEntity(restoreItem)
            noteDataSource.update(entity)
        }
    }

    @Test fun clearNote() {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()
        val rankId = Random.nextLong()
        val entity = mockk<NoteEntity>()

        every { item.id } returns id
        every { item.rank.id } returns rankId
        coEvery { spyRepository.clearConnection(id, rankId) } returns Unit
        every { noteConverter.toEntity(item) } returns entity

        runBlocking {
            spyRepository.clearNote(item)
        }

        coVerifySequence {
            spyRepository.clearNote(item)

            item.id
            item.rank.id
            spyRepository.clearConnection(id, rankId)
            noteConverter.toEntity(item)
            noteDataSource.delete(entity)
        }
    }

    @Test fun clearConnection() {
        val noteId = Random.nextLong()
        val rankId = Random.nextLong()
        val entity = mockk<RankEntity>()
        val noteIdList = mockk<MutableList<Long>>()

        coEvery { rankDataSource.get(rankId) } returns null

        runBlocking {
            repository.clearConnection(noteId, rankId)
        }

        coEvery { rankDataSource.get(rankId) } returns entity
        every { entity.noteId } returns noteIdList
        every { noteIdList.remove(noteId) } returns Random.nextBoolean()

        runBlocking {
            repository.clearConnection(noteId, rankId)
        }

        coVerifySequence {
            rankDataSource.get(rankId)

            rankDataSource.get(rankId)
            entity.noteId
            noteIdList.remove(noteId)
            rankDataSource.update(entity)
        }
    }

    // Repo save and update functions

    @Test fun `convertNote from text to roll`() {
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

        coEvery { rollDataSource.insert(rollEntity) } returns rollId
        every { rollConverter.toEntity(noteId, item = any()) } returns rollEntity
        for (it in rollItemList) {
            every { it.id = rollId } returns Unit
        }

        every { noteConverter.toEntity(finishItem) } returns finishEntity

        runBlocking {
            assertEquals(repository.convertNote(startItem), finishItem)
        }

        coVerifySequence {
            startItem.onConvert()
            finishItem.list
            for (it in rollItemList) {
                finishItem.id

                rollConverter.toEntity(noteId, it)
                rollDataSource.insert(rollEntity)
                it.id = rollId
            }
            noteConverter.toEntity(finishItem)
            noteDataSource.update(finishEntity)

            finishItem == finishItem
        }

    }

    @Test fun `convertNote from roll to text`() {
        val startItem = mockk<NoteItem.Roll>()
        val finishItem = mockk<NoteItem.Text>()
        val finishEntity = mockk<NoteEntity>()

        val id = Random.nextLong()
        val entityList = mockk<List<RollEntity>>()
        val itemList = mockk<MutableList<RollItem>>()

        every { startItem.onConvert() } returns finishItem
        every { noteConverter.toEntity(finishItem) } returns finishEntity
        every { finishItem.id } returns id

        runBlocking {
            assertEquals(repository.convertNote(startItem), finishItem)
        }

        every { startItem.id } returns id
        coEvery { rollDataSource.getList(id) } returns entityList
        every { rollConverter.toItem(entityList) } returns itemList
        //        every { startItem.onConvert(itemList) } returns finishItem

        runBlocking {
            assertEquals(repository.convertNote(startItem), finishItem)
        }

        coVerifySequence {
            startItem.onConvert()
            noteConverter.toEntity(finishItem)
            noteDataSource.update(finishEntity)
            finishItem.id
            rollDataSource.delete(id)
            finishItem == finishItem

            startItem.id
            rollDataSource.getList(id)
            rollConverter.toItem(entityList)
            //            startItem.onConvert(itemList)
            noteConverter.toEntity(finishItem)
            noteDataSource.update(finishEntity)
            finishItem.id
            rollDataSource.delete(id)
            finishItem == finishItem
        }
    }

    @Test fun `saveNote text`() {
        val item = mockk<NoteItem.Text>()
        val entity = mockk<NoteEntity>()
        val id = Random.nextLong()

        every { noteConverter.toEntity(item) } returns entity

        runBlocking {
            repository.saveNote(item, isCreate = false)
        }

        coEvery { noteDataSource.insert(entity) } returns null

        runBlocking {
            repository.saveNote(item, isCreate = true)
        }

        coEvery { noteDataSource.insert(entity) } returns id
        every { item.id = id } returns Unit

        runBlocking {
            repository.saveNote(item, isCreate = true)
        }

        coVerifySequence {
            noteConverter.toEntity(item)
            noteDataSource.update(entity)

            noteConverter.toEntity(item)
            noteDataSource.insert(entity)

            noteConverter.toEntity(item)
            noteDataSource.insert(entity)
            item.id = id
        }
    }

    @Test fun `saveNote roll onCreate`() {
        val item = mockk<NoteItem.Roll>()
        val entity = mockk<NoteEntity>()

        val id = Random.nextLong()
        val size = getRandomSize()
        val itemList = MutableList<RollItem>(size) { mockk() }
        val entityList = MutableList<RollEntity>(size) { mockk() }
        val idList = List(size) { Random.nextLong() }

        every { noteConverter.toEntity(item) } returns entity
        coEvery { noteDataSource.insert(entity) } returns null

        runBlocking {
            repository.saveNote(item, isCreate = true)
        }

        coEvery { noteDataSource.insert(entity) } returns id
        every { item.id = id } returns Unit
        every { item.id } returns id
        every { item.list } returns itemList

        for ((i, rollItem) in itemList.withIndex()) {
            every { rollConverter.toEntity(id, rollItem) } returns entityList[i]
            coEvery { rollDataSource.insert(entityList[i]) } returns idList[i]
            every { rollItem.id = idList[i] } returns Unit
        }

        runBlocking {
            repository.saveNote(item, isCreate = true)
        }

        /**
         * Can't create verify sequence because of list items (equals checks in log).
         */
        coVerifyOrder {
            noteConverter.toEntity(item)
            noteDataSource.insert(entity)

            noteConverter.toEntity(item)
            noteDataSource.insert(entity)
            item.id = id
            item.list

            for ((i, rollItem) in itemList.withIndex()) {
                item.id
                rollConverter.toEntity(id, rollItem)
                rollDataSource.insert(entityList[i])
                rollItem.id = idList[i]
            }
        }
    }

    @Test fun `saveNote roll onUpdate`() {
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
                coEvery { rollDataSource.insert(entityList[i]) } returns idList[i]
                every { rollItem.id = idList[i] } returns Unit
            } else {
                val itemId = indexToId(i)
                idSaveList.add(itemId)

                every { rollItem.id } returns itemId
                every { rollItem.position } returns positionList[i]
                every { rollItem.text } returns textList[i]
            }
        }

        coEvery { rollDataSource.delete(id, idSaveList) } returns Unit

        runBlocking {
            repository.saveNote(item, isCreate = false)
        }

        /**
         * Can't create verify sequence because of list items (equals checks in log).
         */
        coVerify {
            noteConverter.toEntity(item)
            noteDataSource.update(entity)
            item.list

            for ((i, rollItem) in itemList.withIndex()) {
                rollItem.id

                if (i.isDivideEntirely()) {
                    item.id
                    rollConverter.toEntity(id, rollItem)
                    rollDataSource.insert(entityList[i])
                    rollItem.id = idList[i]
                } else {
                    item.id
                    rollItem.position
                    rollItem.text
                    rollDataSource.update(indexToId(i), positionList[i], textList[i])
                }

                rollItem.id
            }

            item.id
            rollDataSource.delete(id, idSaveList)
        }
    }

    @Test fun `updateRollCheck for item`() {
        val item = mockk<NoteItem.Roll>()
        val p = (0..10).random()
        val list = mockk<MutableList<RollItem>>()
        val rollItem = mockk<RollItem>()
        val rollId = Random.nextLong()
        val isCheck = Random.nextBoolean()
        val entity = mockk<NoteEntity>()

        every { item.list } returns list
        every { list.size } returns 0

        runBlocking {
            repository.updateRollCheck(item, p)
        }

        every { list.size } returns 11
        every { list[p] } returns rollItem
        every { rollItem.id } returns null

        runBlocking {
            repository.updateRollCheck(item, p)
        }

        every { rollItem.id } returns rollId
        every { rollItem.isCheck } returns isCheck
        every { noteConverter.toEntity(item) } returns entity

        runBlocking {
            repository.updateRollCheck(item, p)
        }

        coVerifySequence {
            item.list
            list.size

            item.list
            list.size
            list[p]
            rollItem.id

            item.list
            list.size
            list[p]
            rollItem.id
            rollItem.isCheck
            rollDataSource.updateCheck(rollId, isCheck)
            noteConverter.toEntity(item)
            noteDataSource.update(entity)
        }
    }

    @Test fun updateNote() {
        val item = mockk<NoteItem.Roll>()
        val entity = mockk<NoteEntity>()

        every { noteConverter.toEntity(item) } returns entity

        runBlocking {
            repository.updateNote(item)
        }

        coVerifySequence {
            noteConverter.toEntity(item)
            noteDataSource.update(entity)
        }
    }

    @Test fun insertOrUpdateVisible() {
        val item = mockk<NoteItem.Roll>()
        val id = Random.nextLong()
        val isVisible = Random.nextBoolean()
        val entity = RollVisibleEntity(noteId = id, value = !isVisible)

        every { item.id } returns id
        coEvery { rollVisibleDataSource.getVisible(id) } returns null
        every { item.isVisible } returns !isVisible
        coEvery { rollVisibleDataSource.insert(entity) } returns Random.nextLong()

        runBlocking {
            repository.insertOrUpdateVisible(item)
        }

        coEvery { rollVisibleDataSource.getVisible(id) } returns isVisible

        runBlocking {
            repository.insertOrUpdateVisible(item)
        }

        every { item.isVisible } returns isVisible

        runBlocking {
            repository.insertOrUpdateVisible(item)
        }

        coVerifySequence {
            item.id
            rollVisibleDataSource.getVisible(id)
            item.id
            item.isVisible
            rollVisibleDataSource.insert(entity)

            item.id
            rollVisibleDataSource.getVisible(id)
            item.isVisible
            item.id
            item.isVisible
            rollVisibleDataSource.update(id, !isVisible)

            item.id
            rollVisibleDataSource.getVisible(id)
            item.isVisible
        }
    }
}