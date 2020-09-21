package sgtmelon.scriptum.data.repository.room

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import kotlin.random.Random

/**
 * Test for [RankRepo].
 */
@ExperimentalCoroutinesApi
class RankRepoTest : ParentRoomRepoTest() {

    private val converter: RankConverter = mockk()

    private val rankRepo by lazy { RankRepo(roomProvider, converter) }
    private val spyRankRepo by lazy { spyk(rankRepo) }

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
        val finishRankList = TestData.Rank.itemList.apply {
            for ((i, it) in withIndex()) {
                it.hasBind = i % 2 == 0
                it.hasNotification = i % 2 == 0
            }
        }

        val entityList = RankConverter().toEntity(finishRankList)
        val itemList = RankConverter().toItem(entityList)

        val data = TestData.Note
        val bindList = with(NoteConverter()) {
            listOf(toEntity(data.firstNote), toEntity(data.secondNote))
        }
        val notBindList = with(NoteConverter()) {
            listOf(toEntity(data.firstNote), toEntity(data.thirdNote))
        }

        val alarmList = listOf(AlarmEntity())

        every { converter.toItem(entityList) } returns itemList
        coEvery { rankDao.get() } returns entityList

        for ((i, it) in entityList.withIndex()) {
            coEvery { noteDao.get(it.noteId) } returns if (i % 2 == 0) bindList else notBindList
            coEvery { alarmDao.get(it.noteId) } returns if (i % 2 == 0) alarmList else listOf()
        }

        assertEquals(finishRankList, rankRepo.getList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.get()
            converter.toItem(entityList)
            for (it in entityList) {
                noteDao.get(it.noteId)
                alarmDao.get(it.noteId)
            }
        }
    }

    @Test fun getBind() = startCoTest {
        val bindIdList = List(size = 5) { Random.nextLong() }
        val notBindIdList = List(size = 2) { Random.nextLong() }

        val data = TestData.Note
        val bindList = with(NoteConverter()) {
            listOf(toEntity(data.firstNote), toEntity(data.secondNote))
        }
        val notBindList = with(NoteConverter()) {
            listOf(toEntity(data.firstNote), toEntity(data.thirdNote))
        }

        coEvery { noteDao.get(bindIdList) } returns bindList
        coEvery { noteDao.get(notBindIdList) } returns notBindList

        assertEquals(true, rankRepo.getBind(bindIdList))
        assertEquals(false, rankRepo.getBind(notBindIdList))

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.get(bindIdList)

            roomProvider.openRoom()
            noteDao.get(notBindIdList)
        }
    }

    @Test fun getIdVisibleList() = startCoTest {
        val idVisibleList = List(size = 10) { Random.nextLong() }

        coEvery { rankDao.getIdVisibleList() } returns idVisibleList

        assertEquals(idVisibleList, rankRepo.getIdVisibleList())

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getIdVisibleList()
        }
    }


    @Test fun insert_byName() = startCoTest {
        val id = Random.nextLong()
        val name = nextString()
        val entity = RankEntity(name = name)

        coEvery { rankDao.insert(entity) } returns id

        every { roomDb.checkInsertIgnore(id) } returns null
        assertNull(rankRepo.insert(name))

        every { roomDb.checkInsertIgnore(id) } returns id
        assertEquals(id, rankRepo.insert(name))

        coVerifySequence {
            repeat(times = 2) {
                roomProvider.openRoom()

                roomDb.rankDao
                rankDao.insert(entity)
                roomDb.checkInsertIgnore(id)
                roomDb.close()
            }
        }
    }

    @Suppress("RemoveExplicitTypeArguments")
    @Test fun insert_byItem() = startCoTest {
        val rankItem = mockk<RankItem>()
        val rankEntity = mockk<RankEntity>()

        val idList = MutableList(size = 5) { Random.nextLong() }
        val id = Random.nextLong()
        val position = Random.nextInt()

        val noteEntityList: MutableList<NoteEntity?> = MutableList(size = 5) {
            if (it == 0) null else mockk<NoteEntity>()
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
        coEvery { rankDao.insert(rankEntity) } returns id

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
            rankDao.insert(rankEntity)
        }
    }


    @Test fun delete() = startCoTest {
        val item = TestData.Rank.firstRank
        val entity = NoteConverter().toEntity(TestData.Note.firstNote)

        coEvery { noteDao.get(item.noteId.first()) } returns null
        coEvery { noteDao.get(item.noteId.last()) } returns entity

        rankRepo.delete(item)

        assertEquals(DbData.Note.Default.RANK_ID, entity.rankId)
        assertEquals(DbData.Note.Default.RANK_PS, entity.rankPs)

        coVerifySequence {
            roomProvider.openRoom()
            noteDao.get(item.noteId.first())
            noteDao.get(item.noteId.last())
            noteDao.update(entity)
            rankDao.delete(item.name)
        }
    }

    @Test fun updateItem() = startCoTest {
        val item = TestData.Rank.itemList.random()
        val entity = RankConverter().toEntity(item)

        every { converter.toEntity(item) } returns entity

        rankRepo.update(item)

        coVerifySequence {
            roomProvider.openRoom()
            converter.toEntity(item)
            rankDao.update(entity)
        }
    }

    @Test fun updateList() = startCoTest {
        val itemList = TestData.Rank.itemList
        val entityList = RankConverter().toEntity(itemList)

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

        coEvery { spyRankRepo.updateRankPosition(noteDao, rankList, noteIdList) } returns Unit
        every { converter.toEntity(rankList) } returns entityList

        spyRankRepo.updatePosition(rankList, noteIdList)

        coVerifySequence {
            spyRankRepo.updatePosition(rankList, noteIdList)
            spyRankRepo.inRoom(any())
            spyRankRepo.roomProvider

            roomProvider.openRoom()
            spyRankRepo.updateRankPosition(noteDao, rankList, noteIdList)
            converter.toEntity(rankList)
            rankDao.update(entityList)
        }
    }

    @Test fun updateRankPosition() = startCoTest {
        val rankItemList = TestData.Rank.itemList
        val noteIdList = List(size = 5) { Random.nextLong() }

        val noteData = TestData.Note
        val noteList = with(NoteConverter()) {
            listOf(
                    toEntity(noteData.firstNote),
                    toEntity(noteData.firstNote.deepCopy(rankId = 1)),
                    toEntity(noteData.secondNote.deepCopy(rankId = 3))
            )
        }

        coEvery { noteDao.get(noteIdList) } returns noteList

        rankRepo.updateRankPosition(noteDao, rankItemList, noteIdList)
        assertEquals(-1, noteList[0].rankPs)
        assertEquals(0, noteList[1].rankPs)
        assertEquals(2, noteList[2].rankPs)

        coVerifySequence {
            noteDao.get(noteIdList)
            noteDao.update(noteList)
        }
    }


    @Test fun updateConnection() = startCoTest {
        val noteItem = mockk<NoteItem>()
        val id = Random.nextLong()
        val rankId = Random.nextLong()
        val getList = mockk<List<RankEntity>>()
        val updateList = mockk<List<RankEntity>>()
        val checkArray = BooleanArray(size = 5) { Random.nextBoolean() }

        every { noteItem.id } returns id
        every { noteItem.rankId } returns rankId
        coEvery { rankDao.get() } returns getList
        every { spyRankRepo.calculateCheckArray(getList, rankId) } returns checkArray
        every { spyRankRepo.updateNoteId(getList, checkArray, id) } returns updateList

        spyRankRepo.updateConnection(noteItem)

        coVerifySequence {
            spyRankRepo.updateConnection(noteItem)
            spyRankRepo.inRoom(any())
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
        val nameList = List(size = 5) { nextString() }
        val nameArray = nameList.toTypedArray()

        coEvery { rankDao.getNameList() } returns nameList.subList(1, 5)

        assertArrayEquals(nameArray, rankRepo.getDialogItemArray(nameList.first()))

        coVerifySequence {
            roomProvider.openRoom()
            rankDao.getNameList()
        }
    }

    @Test fun getId() = startCoTest {
        val defaultId = DbData.Note.Default.RANK_ID
        val id = Random.nextLong()

        val defaultPosition = DbData.Note.Default.RANK_PS
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