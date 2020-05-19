package sgtmelon.scriptum.data.repository.room

import io.mockk.coEvery
import io.mockk.coVerifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentRoomRepoTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.data.DbData
import kotlin.random.Random

/**
 * Test for [RankRepo].
 */
@ExperimentalCoroutinesApi
class RankRepoTest : ParentRoomRepoTest() {

    private val badRankRepo by lazy { RankRepo(badRoomProvider) }
    private val goodRankRepo by lazy { RankRepo(goodRoomProvider) }

    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { rankDao.getCount() } returns count

        assertNull(badRankRepo.getCount())
        assertEquals(count, goodRankRepo.getCount())

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.getCount()
        }
    }

    @Test fun getList() = startCoTest {
        val finishRankList = TestData.Rank.itemList.apply {
            forEachIndexed { i, it ->
                it.hasBind = i % 2 == 0
                it.hasNotification = i % 2 == 0
            }
        }
        val entityList = RankConverter().toEntity(finishRankList)

        val data = TestData.Note
        val bindList = with(NoteConverter()) {
            listOf(toEntity(data.firstNote), toEntity(data.secondNote))
        }
        val notBindList = with(NoteConverter()) {
            listOf(toEntity(data.firstNote), toEntity(data.thirdNote))
        }

        val alarmList = listOf(AlarmEntity())

        coEvery { rankDao.get() } returns entityList

        entityList.forEachIndexed { i, it ->
            coEvery { noteDao.get(it.noteId) } returns if (i % 2 == 0) bindList else notBindList
            coEvery { alarmDao.get(it.noteId) } returns if (i % 2 == 0) alarmList else listOf()
        }

        assertNull(badRankRepo.getList())
        assertEquals(finishRankList, goodRankRepo.getList())

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.get()
            entityList.forEach {
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

        assertNull(badRankRepo.getBind(bindIdList))
        assertEquals(true, goodRankRepo.getBind(bindIdList))
        assertEquals(false, goodRankRepo.getBind(notBindIdList))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.get(bindIdList)

            goodRoomProvider.openRoom()
            noteDao.get(notBindIdList)
        }
    }

    @Test fun getIdVisibleList() = startCoTest {
        val idVisibleList = List(size = 10) { Random.nextLong() }

        coEvery { rankDao.getIdVisibleList() } returns idVisibleList

        assertNull(badRankRepo.getIdVisibleList())
        assertEquals(idVisibleList, goodRankRepo.getIdVisibleList())

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.getIdVisibleList()
        }
    }


    @Test fun insert() = startCoTest {
        val id = Random.nextLong()
        val name = Random.nextString()
        val entity = RankEntity(name = name)

        coEvery { rankDao.insert(entity) } returns id

        assertNull(badRankRepo.insert(name))
        assertEquals(id, goodRankRepo.insert(name))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.insert(entity)
        }
    }

    @Test fun delete() = startCoTest {
        val item = TestData.Rank.firstRank
        val entity = NoteConverter().toEntity(TestData.Note.firstNote)

        coEvery { noteDao.get(item.noteId.first()) } returns null
        coEvery { noteDao.get(item.noteId.last()) } returns entity

        badRankRepo.delete(item)
        goodRankRepo.delete(item)

        assertEquals(DbData.Note.Default.RANK_ID, entity.rankId)
        assertEquals(DbData.Note.Default.RANK_PS, entity.rankPs)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            noteDao.get(item.noteId.first())
            noteDao.get(item.noteId.last())
            noteDao.update(entity)
            rankDao.delete(item.name)
        }
    }

    @Test fun updateItem() = startCoTest {
        val item = TestData.Rank.itemList.random()
        val entity = RankConverter().toEntity(item)

        badRankRepo.update(item)
        goodRankRepo.update(item)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.update(entity)
        }
    }

    @Test fun updateList() = startCoTest {
        val itemList = TestData.Rank.itemList
        val entityList = RankConverter().toEntity(itemList)

        badRankRepo.update(itemList)
        goodRankRepo.update(itemList)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.update(entityList)
        }
    }

    @Test fun updatePosition() = startCoTest {
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
        val rankEntityList = RankConverter().toEntity(rankItemList)

        coEvery { noteDao.get(noteIdList) } returns noteList

        badRankRepo.updatePosition(rankItemList, noteIdList)
        assertEquals(-1, noteList[0].rankPs)
        assertNotEquals(0, noteList[1].rankPs)
        assertNotEquals(2, noteList[2].rankPs)

        goodRankRepo.updatePosition(rankItemList, listOf())
        assertEquals(-1, noteList[0].rankPs)
        assertNotEquals(0, noteList[1].rankPs)
        assertNotEquals(2, noteList[2].rankPs)

        goodRankRepo.updatePosition(rankItemList, noteIdList)
        assertEquals(-1, noteList[0].rankPs)
        assertEquals(0, noteList[1].rankPs)
        assertEquals(2, noteList[2].rankPs)

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.update(rankEntityList)

            goodRoomProvider.openRoom()
            noteDao.get(noteIdList)
            noteDao.update(noteList)
            rankDao.update(rankEntityList)
        }
    }


    @Test fun updateConnection() = startCoTest {
        val item = TestData.Note.fourthNote

        val rankEntityList = RankConverter().toEntity(TestData.Rank.itemList.apply {
            get(1).noteId.remove(item.id)
        })

        coEvery { rankDao.get() } returns rankEntityList

        badRankRepo.updateConnection(item)

        assertFalse(rankEntityList[0].noteId.contains(item.id))
        assertFalse(rankEntityList[1].noteId.contains(item.id))
        assertFalse(rankEntityList[2].noteId.contains(item.id))
        assertFalse(rankEntityList[3].noteId.contains(item.id))

        goodRankRepo.updateConnection(item)

        assertFalse(rankEntityList[0].noteId.contains(item.id))
        assertTrue(rankEntityList[1].noteId.contains(item.id))
        assertFalse(rankEntityList[2].noteId.contains(item.id))
        assertFalse(rankEntityList[3].noteId.contains(item.id))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.get()
            rankDao.update(rankEntityList)
        }
    }

    @Test fun calculateCheckArray() {
        val list = RankConverter().toEntity(TestData.Rank.itemList)

        with(goodRankRepo) {
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

        with(goodRankRepo) {
            val list = getList()
            val id = Random.nextLong()
            val array = booleanArrayOf()

            updateNoteId(list, array, id)
        }

        with(goodRankRepo) {
            val id = Random.nextLong()
            fun assertUnique(list: List<RankEntity>, array: BooleanArray, id: Long) {
                array.forEachIndexed { i, b ->
                    assertEquals(if (b) 3 else 2, list[i].noteId.size)
                    assertEquals(b, list[i].noteId.contains(id))
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

        with(goodRankRepo) {
            val list = getList()
            val id = list.first().noteId.random()
            val array = booleanArrayOf(true, false, false, false)

            updateNoteId(list, array, id)

            assertEquals(2, list.first().noteId.size)
            assertTrue(list.first().noteId.contains(id))
        }

        with(goodRankRepo) {
            val list = getList()
            val id = list.first().noteId.random()
            val array = booleanArrayOf(false, false, false, false)

            updateNoteId(list, array, id)

            assertEquals(1, list.first().noteId.size)
            assertFalse(list.first().noteId.contains(id))
        }
    }


    @Test fun getDialogItemArray() = startCoTest {
        val nameList = List(size = 5) { Random.nextString() }
        val nameArray = nameList.toTypedArray()

        coEvery { rankDao.getNameList() } returns nameList.subList(1, 5)

        assertNull(badRankRepo.getDialogItemArray(nameList.first()))
        assertArrayEquals(nameArray, goodRankRepo.getDialogItemArray(nameList.first()))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.getNameList()
        }
    }

    @Test fun getId() = startCoTest {
        val defaultId = DbData.Note.Default.RANK_ID
        val id = Random.nextLong()

        val defaultPosition = DbData.Note.Default.RANK_PS
        val position = Random.nextInt()

        assertEquals(defaultId, badRankRepo.getId(defaultPosition))
        assertNull(badRankRepo.getId(position))

        coEvery { rankDao.getId(position) } returns null
        assertEquals(defaultId, goodRankRepo.getId(position))

        coEvery { rankDao.getId(position) } returns id
        assertEquals(id, goodRankRepo.getId(position))

        coVerifySequence {
            badRoomProvider.openRoom()

            goodRoomProvider.openRoom()
            rankDao.getId(position)

            goodRoomProvider.openRoom()
            rankDao.getId(position)
        }
    }

}