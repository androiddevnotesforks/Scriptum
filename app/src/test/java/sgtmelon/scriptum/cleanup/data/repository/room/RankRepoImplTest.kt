package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.test.common.isDivideEntirely
import sgtmelon.test.common.nextString

/**
 * Test for [RankRepoImpl].
 */
class RankRepoImplTest : ParentRepoTest() {

    private val converter: RankConverter = mockk()

    private val repo by lazy {
        RankRepoImpl(noteDataSource, rankDataSource, alarmDataSource, converter)
    }
    private val spyRepo by lazy { spyk(repo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(converter)
    }

    @Test fun getCount() {
        val count = Random.nextInt()

        coEvery { rankDataSource.getCount() } returns count

        runBlocking {
            assertEquals(repo.getCount(), count)
        }

        coVerifySequence {
            rankDataSource.getCount()
        }
    }

    @Test fun getList() {
        val entityList = mockk<List<RankEntity>>()
        val size = getRandomSize()
        val itemList = MutableList<RankItem>(size) { mockk() }

        val noteIdList = List<MutableList<Long>>(size) { mockk() }
        val bindCountList = List(size) { Random.nextInt() }
        val notificationCountList = List(size) { Random.nextInt() }

        coEvery { rankDataSource.getList() } returns entityList
        every { converter.toItem(entityList) } returns itemList

        for ((i, item) in itemList.withIndex()) {
            every { item.noteId } returns noteIdList[i]
            coEvery { noteDataSource.getBindCount(noteIdList[i]) } returns bindCountList[i]
            every { item.bindCount = bindCountList[i] } returns Unit

            coEvery { alarmDataSource.getCount(noteIdList[i]) } returns notificationCountList[i]
            every { item.notificationCount = notificationCountList[i] } returns Unit
        }

        runBlocking {
            assertEquals(repo.getList(), itemList)
        }

        coVerifySequence {
            rankDataSource.getList()
            converter.toItem(entityList)

            for ((i, item) in itemList.withIndex()) {
                item.noteId
                noteDataSource.getBindCount(noteIdList[i])
                item.bindCount = bindCountList[i]

                item.noteId
                alarmDataSource.getCount(noteIdList[i])
                item.notificationCount = notificationCountList[i]
            }
        }
    }

    @Test fun getIdVisibleList() {
        val idVisibleList = mockk<List<Long>>()

        coEvery { rankDataSource.getIdVisibleList() } returns idVisibleList

        runBlocking {
            assertEquals(repo.getIdVisibleList(), idVisibleList)
        }

        coVerifySequence {
            rankDataSource.getIdVisibleList()
        }
    }


    @Test fun `insert by name`() {
        val id = Random.nextLong()
        val name = nextString()
        val entity = mockk<RankEntity>()
        val item = RankItem(id, name = name)

        FastMock.daoExtension()
        mockkObject(RankEntity)
        every { RankEntity[name] } returns entity

        coEvery { rankDataSource.insert(entity) } returns null

        runBlocking {
            assertNull(repo.insert(name))
        }

        coEvery { rankDataSource.insert(entity) } returns id

        runBlocking {
            assertEquals(repo.insert(name), item)
        }

        coVerifySequence {
            repeat(times = 2) {
                RankEntity[name]
                rankDataSource.insert(entity)
            }
        }
    }

    @Test fun `insert by item`() {
        val rankItem = mockk<RankItem>()
        val rankEntity = mockk<RankEntity>()

        val size = getRandomSize()
        val idList = MutableList(size) { Random.nextLong() }
        val id = Random.nextLong()
        val position = Random.nextInt()

        val noteEntityList: MutableList<NoteEntity?> = MutableList(size) {
            if (it == 0) null else mockk()
        }

        every { rankItem.noteId } returns idList
        every { rankItem.id } returns id
        every { rankItem.position } returns position

        for ((i, it) in noteEntityList.withIndex()) {
            coEvery { noteDataSource.get(idList[i]) } returns it

            if (it != null) {
                every { it.rankId = id } returns Unit
                every { it.rankPs = position } returns Unit
            }
        }

        every { converter.toEntity(rankItem) } returns rankEntity
        FastMock.daoExtension()
        coEvery { rankDataSource.insert(rankEntity) } returns if (Random.nextBoolean()) id else null

        runBlocking {
            repo.insert(rankItem)
        }

        coVerifySequence {
            rankItem.noteId

            for ((i, it) in noteEntityList.withIndex()) {
                noteDataSource.get(idList[i])

                if (it != null) {
                    rankItem.id
                    it.rankId = id
                    rankItem.position
                    it.rankPs = position

                    noteDataSource.update(it)
                }
            }

            converter.toEntity(rankItem)
            rankDataSource.insert(rankEntity)
        }
    }

    @Test fun delete() {
        val item = mockk<RankItem>()
        val size = getRandomSize()
        val idList = MutableList(size) { Random.nextLong() }
        val entityList = List<NoteEntity>(size) { mockk() }
        val name = nextString()

        every { item.noteId } returns idList
        every { item.name } returns name

        for ((i, id) in idList.withIndex()) {
            val isValid = i.isDivideEntirely()

            coEvery { noteDataSource.get(id) } returns if (isValid) entityList[i] else null

            if (isValid) {
                every { entityList[i].rankId = Note.Default.RANK_ID } returns Unit
                every { entityList[i].rankPs = Note.Default.RANK_PS } returns Unit
            }
        }

        runBlocking {
            repo.delete(item)
        }

        coVerifySequence {
            item.noteId

            for ((i, id) in idList.withIndex()) {
                val isValid = i.isDivideEntirely()

                noteDataSource.get(id)

                if (isValid) {
                    entityList[i].rankId = Note.Default.RANK_ID
                    entityList[i].rankPs = Note.Default.RANK_PS
                    noteDataSource.update(entityList[i])
                }
            }

            item.name
            rankDataSource.delete(item)
        }
    }

    @Test fun `update item`() {
        val item = mockk<RankItem>()
        val entity = mockk<RankEntity>()

        every { converter.toEntity(item) } returns entity

        runBlocking {
            repo.update(item)
        }

        coVerifySequence {
            converter.toEntity(item)
            rankDataSource.update(entity)
        }
    }

    @Test fun `update list`() {
        val itemList = mockk<List<RankItem>>()
        val entityList = mockk<MutableList<RankEntity>>()

        every { converter.toEntity(itemList) } returns entityList

        runBlocking {
            repo.update(itemList)
        }

        coVerifySequence {
            converter.toEntity(itemList)
            rankDataSource.update(entityList)
        }
    }

    @Test fun updatePosition() {
        val rankList = mockk<List<RankItem>>()
        val noteIdList = mockk<List<Long>>()

        val entityList = mockk<MutableList<RankEntity>>()

        coEvery { spyRepo.updateRankPositionsForNotes(rankList, noteIdList) } returns Unit
        every { converter.toEntity(rankList) } returns entityList

        runBlocking {
            spyRepo.updatePositions(rankList, noteIdList)
        }

        coVerifySequence {
            spyRepo.updatePositions(rankList, noteIdList)

            spyRepo.updateRankPositionsForNotes(rankList, noteIdList)
            converter.toEntity(rankList)
            rankDataSource.update(entityList)
        }
    }

    @Test fun updateRankPositionsForNotes() {
        val size = getRandomSize()
        val list = List<RankItem>(size) { mockk() }
        val noteIdList = mockk<List<Long>>()

        val entityList = List<NoteEntity>(size) { mockk() }
        val rankIdList = List(size) { Random.nextLong() }
        val rankPsList = List(size) { Random.nextInt() }

        every { noteIdList.isEmpty() } returns true

        runBlocking {
            repo.updateRankPositionsForNotes(list, noteIdList)
        }

        every { noteIdList.isEmpty() } returns false
        coEvery { noteDataSource.getList(noteIdList) } returns entityList

        for ((i, entity) in entityList.withIndex()) {
            every { list[i].id } returns rankIdList[i]

            val isFind = i.isDivideEntirely()
            every { entity.rankId } returns if (isFind) rankIdList[i] else -1
            if (isFind) {
                every { list[i].position } returns rankPsList[i]
                every { entity.rankPs = rankPsList[i] } returns Unit
            }
        }

        runBlocking {
            repo.updateRankPositionsForNotes(list, noteIdList)
        }

        coVerifySequence {
            noteIdList.isEmpty()

            noteIdList.isEmpty()
            noteDataSource.getList(noteIdList)

            for ((i, entity) in entityList.withIndex()) {
                val isFind = i.isDivideEntirely()

                for ((j, item) in list.withIndex()) {
                    item.id
                    entity.rankId

                    if (isFind && i == j) {
                        item.position
                        entity.rankPs = rankPsList[i]
                        break
                    }
                }
            }

            noteDataSource.update(entityList)
        }
    }


    @Test fun updateConnection() {
        val noteItem = mockk<NoteItem>()
        val id = Random.nextLong()
        val rankId = Random.nextLong()
        val getList = mockk<List<RankEntity>>()
        val updateList = mockk<List<RankEntity>>()
        val size = getRandomSize()
        val checkArray = BooleanArray(size) { Random.nextBoolean() }

        every { noteItem.id } returns id
        every { noteItem.rankId } returns rankId
        coEvery { rankDataSource.getList() } returns getList
        every { spyRepo.calculateCheckArray(getList, rankId) } returns checkArray
        every { spyRepo.updateNoteId(getList, checkArray, id) } returns updateList

        runBlocking {
            spyRepo.updateConnection(noteItem)
        }

        coVerifySequence {
            spyRepo.updateConnection(noteItem)

            rankDataSource.getList()
            noteItem.rankId
            spyRepo.calculateCheckArray(getList, rankId)
            noteItem.id
            spyRepo.updateNoteId(getList, checkArray, id)
            rankDataSource.update(updateList)
        }
    }

    @Test fun calculateCheckArray() {
        val list = RankConverter().toEntity(TestData.Rank.itemList)

        with(repo) {
            var array = booleanArrayOf(false, false, false, false)
            assertArrayEquals(array, calculateCheckArray(list, rankId = 0))

            array = booleanArrayOf(true, false, false, false)
            assertArrayEquals(array, calculateCheckArray(list, rankId = 1))

            array = booleanArrayOf(false, true, false, false)
            assertArrayEquals(array, calculateCheckArray(list, rankId = 2))

            array = booleanArrayOf(false, false, true, false)
            assertArrayEquals(array, calculateCheckArray(list, rankId = 3))

            array = booleanArrayOf(false, false, false, true)
            assertArrayEquals(array, calculateCheckArray(list, rankId = 4))
        }
    }

    @Test fun updateNoteId() {
        fun getList() = RankConverter().toEntity(TestData.Rank.itemList)

        with(repo) {
            val list = getList()
            val id = Random.nextLong()
            val array = booleanArrayOf()

            updateNoteId(list, array, id)
        }

        with(repo) {
            val id = Random.nextLong()
            fun assertUnique(list: List<RankEntity>, array: BooleanArray, id: Long) {
                for ((i, bool) in array.withIndex()) {
                    assertEquals(if (bool) 3 else 2, list[i].noteId.size)
                    assertEquals(bool, list[i].noteId.contains(id))
                }
            }

            getList().let {
                val array = booleanArrayOf(false, false, false, false)
                updateNoteId(it, array, id)
                updateNoteId(it, array, id)
                assertUnique(it, array, id)
            }

            getList().let {
                val array = booleanArrayOf(true, false, false, false)
                updateNoteId(it, array, id)
                updateNoteId(it, array, id)
                assertUnique(it, array, id)
            }

            getList().let {
                val array = booleanArrayOf(false, true, false, false)
                updateNoteId(it, array, id)
                updateNoteId(it, array, id)
                assertUnique(it, array, id)
            }

            getList().let {
                val array = booleanArrayOf(false, false, true, false)
                updateNoteId(it, array, id)
                updateNoteId(it, array, id)
                assertUnique(it, array, id)
            }

            getList().let {
                val array = booleanArrayOf(false, false, false, true)
                updateNoteId(it, array, id)
                updateNoteId(it, array, id)
                assertUnique(it, array, id)
            }
        }

        with(repo) {
            val list = getList()
            val id = list.first().noteId.random()
            val array = booleanArrayOf(true, false, false, false)

            updateNoteId(list, array, id)

            assertEquals(2, list.first().noteId.size)
            assertTrue(list.first().noteId.contains(id))
        }

        with(repo) {
            val list = getList()
            val id = list.first().noteId.random()
            val array = booleanArrayOf(false, false, false, false)

            updateNoteId(list, array, id)

            assertEquals(1, list.first().noteId.size)
            assertFalse(list.first().noteId.contains(id))
        }
    }


    @Test fun getDialogItemArray() {
        val size = getRandomSize()
        val nameList = List(size) { nextString() }
        val nameArray = nameList.toTypedArray()

        coEvery { rankDataSource.getNameList() } returns nameList.takeLast(n = size - 1)

        runBlocking {
            assertArrayEquals(repo.getDialogItemArray(nameList.first()), nameArray)
        }

        coVerifySequence {
            rankDataSource.getNameList()
        }
    }

    @Test fun getId() {
        val defaultId = Note.Default.RANK_ID
        val id = Random.nextLong()

        val defaultPosition = Note.Default.RANK_PS
        val position = Random.nextInt()

        runBlocking {
            assertEquals(repo.getId(defaultPosition), defaultId)
        }

        coEvery { rankDataSource.getId(position) } returns null

        runBlocking {
            assertEquals(repo.getId(position), defaultId)
        }

        coEvery { rankDataSource.getId(position) } returns id

        runBlocking {
            assertEquals(repo.getId(position), id)
        }

        coVerifySequence {
            rankDataSource.getId(position)
            rankDataSource.getId(position)
        }
    }


    @Test fun getRankBackup() {
        val rankList = mockk<List<RankEntity>>()

        coEvery { rankDataSource.getList() } returns rankList

        runBlocking {
            assertEquals(repo.getRankBackup(), rankList)
        }

        coVerifySequence {
            rankDataSource.getList()
        }
    }
}