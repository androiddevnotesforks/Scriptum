package sgtmelon.scriptum.cleanup.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.data.room.converter.model.RankConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.extension.safeInsert
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.isDivideTwoEntirely
import sgtmelon.scriptum.cleanup.parent.ParentRoomRepoTest

/**
 * Test for [RankRepo].
 */
@ExperimentalCoroutinesApi
class RankRepoTest : ParentRoomRepoTest() {

    private val converter: RankConverter = mockk()

    private val rankRepo by lazy { RankRepo(roomProvider, converter) }
    private val spyRankRepo by lazy { spyk(rankRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(converter)
    }

    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { rankDao.getCount() } returns count

        assertEquals(count, rankRepo.getCount())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getCount()
        }
    }

    @Test fun getList() = startCoTest {
        val entityList = mockk<List<RankEntity>>()
        val size = getRandomSize()
        val itemList = MutableList<RankItem>(size) { mockk() }

        val noteIdList = List<MutableList<Long>>(size) { mockk() }
        val bindCountList = List(size) { Random.nextInt() }
        val notificationCountList = List(size) { Random.nextInt() }

        coEvery { rankDao.get() } returns entityList
        every { converter.toItem(entityList) } returns itemList

        for ((i, item) in itemList.withIndex()) {
            every { item.noteId } returns noteIdList[i]
            coEvery { noteDao.getBindCount(noteIdList[i]) } returns bindCountList[i]
            every { item.bindCount = bindCountList[i] } returns Unit

            coEvery { alarmDao.getCount(noteIdList[i]) } returns notificationCountList[i]
            every { item.notificationCount = notificationCountList[i] } returns Unit
        }

        assertEquals(itemList, rankRepo.getList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.get()
            converter.toItem(entityList)

            for ((i, item) in itemList.withIndex()) {
                item.noteId
                noteDao.getBindCount(noteIdList[i])
                item.bindCount = bindCountList[i]

                item.noteId
                alarmDao.getCount(noteIdList[i])
                item.notificationCount = notificationCountList[i]
            }
        }
    }

    @Test fun getIdVisibleList() = startCoTest {
        val idVisibleList = mockk<List<Long>>()

        coEvery { rankDao.getIdVisibleList() } returns idVisibleList

        assertEquals(idVisibleList, rankRepo.getIdVisibleList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getIdVisibleList()
        }
    }


    @Test fun `insert by name`() = startCoTest {
        val id = Random.nextLong()
        val name = nextString()
        val entity = mockk<RankEntity>()

        FastMock.daoExtension()
        mockkObject(RankEntity)
        every { RankEntity[name] } returns entity

        coEvery { rankDao.safeInsert(entity) } returns null
        assertNull(rankRepo.insert(name))

        coEvery { rankDao.safeInsert(entity) } returns id
        assertEquals(id, rankRepo.insert(name))

        coVerifySequence {
            repeat(times = 2) {
                roomProvider.openRoom()

                roomDb.rankDao
                RankEntity[name]
                rankDao.safeInsert(entity)
                roomDb.close()
            }
        }
    }

    @Test fun `insert by item`() = startCoTest {
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
            coEvery { noteDao.get(idList[i]) } returns it

            if (it != null) {
                every { it.rankId = id } returns Unit
                every { it.rankPs = position } returns Unit
            }
        }

        every { converter.toEntity(rankItem) } returns rankEntity
        FastMock.daoExtension()
        coEvery { rankDao.safeInsert(rankEntity) } returns id

        rankRepo.insert(rankItem)

        coVerifySequence {
            roomProvider.openRoom()
            rankItem.noteId

            for ((i, it) in noteEntityList.withIndex()) {
                noteDao.get(idList[i])

                if (it != null) {
                    rankItem.id
                    it.rankId = id
                    rankItem.position
                    it.rankPs = position

                    noteDao.update(it)
                }
            }

            converter.toEntity(rankItem)
            rankDao.safeInsert(rankEntity)
        }
    }

    @Test fun delete() = startCoTest {
        val item = mockk<RankItem>()
        val size = getRandomSize()
        val idList = MutableList(size) { Random.nextLong() }
        val entityList = List<NoteEntity>(size) { mockk() }
        val name = nextString()

        every { item.noteId } returns idList
        every { item.name } returns name

        for ((i, id) in idList.withIndex()) {
            val isValid = i.isDivideTwoEntirely()

            coEvery { noteDao.get(id) } returns if (isValid) entityList[i] else null

            if (isValid) {
                every { entityList[i].rankId = Note.Default.RANK_ID } returns Unit
                every { entityList[i].rankPs = Note.Default.RANK_PS } returns Unit
            }
        }

        rankRepo.delete(item)

        coVerifySequence {
            roomProvider.openRoom()
            item.noteId

            for ((i, id) in idList.withIndex()) {
                val isValid = i.isDivideTwoEntirely()

                noteDao.get(id)

                if (isValid) {
                    entityList[i].rankId = Note.Default.RANK_ID
                    entityList[i].rankPs = Note.Default.RANK_PS
                    noteDao.update(entityList[i])
                }
            }

            item.name
            rankDao.delete(name)
        }
    }

    @Test fun updateItem() = startCoTest {
        val item = mockk<RankItem>()
        val entity = mockk<RankEntity>()

        every { converter.toEntity(item) } returns entity

        rankRepo.update(item)

        coVerifySequence {
            roomProvider.openRoom()
            converter.toEntity(item)
            rankDao.update(entity)
        }
    }

    @Test fun updateList() = startCoTest {
        val itemList = mockk<List<RankItem>>()
        val entityList = mockk<MutableList<RankEntity>>()

        every { converter.toEntity(itemList) } returns entityList

        rankRepo.update(itemList)

        coVerifySequence {
            roomProvider.openRoom()
            converter.toEntity(itemList)
            rankDao.update(entityList)
        }
    }

    @Test fun updatePosition() = startCoTest {
        val rankList = mockk<List<RankItem>>()
        val noteIdList = mockk<List<Long>>()

        val entityList = mockk<MutableList<RankEntity>>()

        coEvery { spyRankRepo.updateRankPosition(rankList, noteIdList, noteDao) } returns Unit
        every { converter.toEntity(rankList) } returns entityList

        spyRankRepo.updatePosition(rankList, noteIdList)

        coVerifySequence {
            spyRankRepo.updatePosition(rankList, noteIdList)
            spyRankRepo.roomProvider

            roomProvider.openRoom()
            spyRankRepo.updateRankPosition(rankList, noteIdList, noteDao)
            converter.toEntity(rankList)
            rankDao.update(entityList)
        }
    }

    @Test fun updateRankPosition() = startCoTest {
        val size = getRandomSize()
        val list = List<RankItem>(size) { mockk() }
        val noteIdList = mockk<List<Long>>()

        val entityList = List<NoteEntity>(size) { mockk() }
        val rankIdList = List(size) { Random.nextLong() }
        val rankPsList = List(size) { Random.nextInt() }

        every { noteIdList.isEmpty() } returns true
        rankRepo.updateRankPosition(list, noteIdList, noteDao)

        every { noteIdList.isEmpty() } returns false
        coEvery { noteDao.get(noteIdList) } returns entityList

        for ((i, entity) in entityList.withIndex()) {
            every { list[i].id } returns rankIdList[i]

            val isFind = i.isDivideTwoEntirely()
            every { entity.rankId } returns if (isFind) rankIdList[i] else -1
            if (isFind) {
                every { list[i].position } returns rankPsList[i]
                every { entity.rankPs = rankPsList[i] } returns Unit
            }
        }

        rankRepo.updateRankPosition(list, noteIdList, noteDao)

        coVerifySequence {
            noteIdList.isEmpty()

            noteIdList.isEmpty()
            noteDao.get(noteIdList)

            for ((i, entity) in entityList.withIndex()) {
                val isFind = i.isDivideTwoEntirely()

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

            noteDao.update(entityList)
        }
    }


    @Test fun updateConnection() = startCoTest {
        val noteItem = mockk<NoteItem>()
        val id = Random.nextLong()
        val rankId = Random.nextLong()
        val getList = mockk<List<RankEntity>>()
        val updateList = mockk<List<RankEntity>>()
        val size = getRandomSize()
        val checkArray = BooleanArray(size) { Random.nextBoolean() }

        every { noteItem.id } returns id
        every { noteItem.rankId } returns rankId
        coEvery { rankDao.get() } returns getList
        every { spyRankRepo.calculateCheckArray(getList, rankId) } returns checkArray
        every { spyRankRepo.updateNoteId(getList, checkArray, id) } returns updateList

        spyRankRepo.updateConnection(noteItem)

        coVerifySequence {
            spyRankRepo.updateConnection(noteItem)
            spyRankRepo.roomProvider

            roomProvider.openRoom()
            rankDao.get()
            noteItem.rankId
            spyRankRepo.calculateCheckArray(getList, rankId)
            noteItem.id
            spyRankRepo.updateNoteId(getList, checkArray, id)
            rankDao.update(updateList)
        }
    }

    @Test fun calculateCheckArray() {
        val list = RankConverter().toEntity(TestData.Rank.itemList)

        with(rankRepo) {
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

        with(rankRepo) {
            val list = getList()
            val id = Random.nextLong()
            val array = booleanArrayOf()

            updateNoteId(list, array, id)
        }

        with(rankRepo) {
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

        with(rankRepo) {
            val list = getList()
            val id = list.first().noteId.random()
            val array = booleanArrayOf(true, false, false, false)

            updateNoteId(list, array, id)

            assertEquals(2, list.first().noteId.size)
            assertTrue(list.first().noteId.contains(id))
        }

        with(rankRepo) {
            val list = getList()
            val id = list.first().noteId.random()
            val array = booleanArrayOf(false, false, false, false)

            updateNoteId(list, array, id)

            assertEquals(1, list.first().noteId.size)
            assertFalse(list.first().noteId.contains(id))
        }
    }


    @Test fun getDialogItemArray() = startCoTest {
        val size = getRandomSize()
        val nameList = List(size) { nextString() }
        val nameArray = nameList.toTypedArray()

        coEvery { rankDao.getNameList() } returns nameList.takeLast(n = size - 1)

        assertArrayEquals(nameArray, rankRepo.getDialogItemArray(nameList.first()))

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getNameList()
        }
    }

    @Test fun getId() = startCoTest {
        val defaultId = Note.Default.RANK_ID
        val id = Random.nextLong()

        val defaultPosition = Note.Default.RANK_PS
        val position = Random.nextInt()

        assertEquals(defaultId, rankRepo.getId(defaultPosition))

        coEvery { rankDao.getId(position) } returns null
        assertEquals(defaultId, rankRepo.getId(position))

        coEvery { rankDao.getId(position) } returns id
        assertEquals(id, rankRepo.getId(position))

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getId(position)

            roomProvider.openRoom()
            rankDao.getId(position)
        }
    }


    @Test fun getRankBackup() = startCoTest {
        val rankList = mockk<List<RankEntity>>()

        coEvery { rankDao.get() } returns rankList

        assertEquals(rankList, rankRepo.getRankBackup())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.get()
        }
    }
}