package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.RankRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.converter.model.RankConverter
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.data.DbData
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel.Companion.correctPositions
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [RankRepo]
 */
@RunWith(AndroidJUnit4::class)
class RankRepoTest : ParentIntegrationTest()  {

    // TODO nullable tests

    private val badRankRepo: IRankRepo = RankRepo(RoomProvider(context = null))
    private val rankRepo: IRankRepo = RankRepo(RoomProvider(context))

    private val rankConverter = RankConverter()
    private val noteConverter = NoteConverter()


    @Test fun getCount() = inRoomTest {
        TODO("nullable")

        assertEquals(0, badRankRepo.getCount())

        assertEquals(0, rankRepo.getCount())
        rankRepo.insert(data.uniqueString)
        assertEquals(1, rankRepo.getCount())
    }

    @Test fun getList() = inRoomTest {
        TODO("nullable")

        assertTrue(badRankRepo.getList().isEmpty())

        val list = mutableListOf<RankItem>()

        assertEquals(list, rankRepo.getList())

        noteDao.insert(firstNote)
        noteDao.insert(secondNote)
        noteDao.insert(thirdNote)

        alarmDao.insert(thirdAlarm)

        listOf(firstRank, secondRank, thirdRank).forEach {
            assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(it))

            list.add(rankConverter.toItem(it).apply {
                hasBind = noteId.contains(secondNote.id)
                hasNotification = noteId.contains(thirdAlarm.noteId)
            })

            assertEquals(list, rankRepo.getList())
        }
    }

    @Test fun getBind() = inRoomTest {
        TODO("nullable")

        assertFalse(badRankRepo.getBind(List(size = 2) { Random.nextLong() }))
        assertFalse(rankRepo.getBind(List(size = 2) { Random.nextLong() }))

        noteDao.insert(firstNote)
        noteDao.insert(secondNote)
        noteDao.insert(thirdNote)

        alarmDao.insert(thirdAlarm)

        listOf(firstRank, secondRank, thirdRank).forEach {
            rankDao.insert(it)
            assertEquals(it.noteId.contains(secondNote.id), rankRepo.getBind(it.noteId))
        }
    }

    @Test fun getIdVisibleList() = inRoomTest {
        TODO("nullable")

        val idList = listOf(firstRank, secondRank, thirdRank).apply {
            forEach { rankDao.insert(it) }
        }.filter { it.isVisible }.map { it.id }

        assertEquals(idList, rankRepo.getIdVisibleList())
    }


    @Test fun insertWithUnique() = inRoomTest {
        TODO("nullable")

        val name = data.uniqueString

        assertEquals(UNIQUE_ERROR_ID, rankRepo.insert(name))

        assertNotEquals(UNIQUE_ERROR_ID, rankRepo.insert(name))
        assertEquals(UNIQUE_ERROR_ID, rankRepo.insert(name))
    }

    @Test fun delete() = inRoomTest {
        TODO("nullable")

        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(firstNote))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(secondNote))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(firstRank))

        rankRepo.delete(rankConverter.toItem(firstRank))

        assertEquals(firstNote.copy(
                rankId = DbData.Note.Default.RANK_ID,
                rankPs = DbData.Note.Default.RANK_PS
        ), noteDao.get(firstNote.id))

        assertEquals(secondNote.copy(
                rankId = DbData.Note.Default.RANK_ID,
                rankPs = DbData.Note.Default.RANK_PS
        ), noteDao.get(secondNote.id))

        assertTrue(rankRepo.getList().isEmpty())
    }

    @Test fun updateItem() = inRoomTest {
        TODO("nullable")

        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(firstRank))

        assertEquals(listOf(rankConverter.toItem(firstRank)), rankRepo.getList())

        val rankItem = rankConverter.toItem(firstRank.copy(name = "54321", isVisible = true))
        rankRepo.update(rankItem)

        assertEquals(listOf(rankItem), rankRepo.getList())
    }

    @Test fun updateList() = inRoomTest {
        TODO("nullable")

        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(firstRank))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(secondRank))

        assertEquals(rankConverter.toItem(listOf(firstRank, secondRank)), rankRepo.getList())

        val rankList = listOf(
                rankConverter.toItem(firstRank.copy(name = "54321", isVisible = true)),
                rankConverter.toItem(secondRank.copy(name = "98765", isVisible = false))
        )
        rankRepo.update(rankList)

        assertEquals(rankList, rankRepo.getList())
    }


    @Test fun updatePosition() = inRoomTest {
        TODO("nullable")

        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(firstNote))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(secondNote))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(thirdNote))

        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(firstRank))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(secondRank))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(thirdRank))

        val rankList = rankConverter.toItem(listOf(thirdRank, firstRank, secondRank))
        val noteIdList = rankList.correctPositions()

        rankRepo.updatePosition(rankList, noteIdList)

        assertEquals(0, rankList[0].position)
        assertEquals(1, rankList[1].position)
        assertEquals(2, rankList[2].position)

        assertEquals(1, noteDao.get(firstNote.id)?.rankPs)
        assertEquals(1, noteDao.get(secondNote.id)?.rankPs)
        assertEquals(2, noteDao.get(thirdNote.id)?.rankPs)

        assertEquals(rankList, rankConverter.toItem(rankDao.get()))
    }


    @Test fun updateConnection() = inRoomTest {
        TODO("nullable")

        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(firstRank))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(secondRank))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(thirdRank))

        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(firstNote))

        val noteItem = noteConverter.toItem(firstNote.copy(rankId = 3, rankPs = 2))
        rankRepo.updateConnection(noteItem)

        assertEquals(thirdRank.copy(noteId = mutableListOf(noteItem.id)), rankDao.get(thirdRank.id))

        noteItem.clearRank()
        rankRepo.updateConnection(noteItem)

        assertEquals(thirdRank, rankDao.get(thirdRank.id))
    }


    @Test fun getDialogItemArray() = inRoomTest {
        TODO("nullable")

        val emptyName = data.uniqueString
        
        val nameList = listOf(firstRank, secondRank, thirdRank)
                .apply { forEach { rankDao.insert(it) } }
                .map { it.name }
                .toMutableList()
                .apply { add(0, emptyName) }

        assertEquals(nameList, rankRepo.getDialogItemArray(emptyName).toList())
    }

    @Test fun getId() = inRoomTest {
        TODO("nullable")

        assertEquals(DbData.Note.Default.RANK_ID, badRankRepo.getId(Random.nextInt()))

        listOf(firstRank, secondRank, thirdRank).forEach { rankDao.insert(it) }

        assertEquals(DbData.Note.Default.RANK_ID, rankRepo.getId(DbData.Note.Default.RANK_PS))
        assertEquals(firstRank.id, rankRepo.getId(position = 0))
        assertEquals(secondRank.id, rankRepo.getId(position = 1))
        assertEquals(thirdRank.id, rankRepo.getId(position = 2))
    }

    private val firstRank = RankEntity(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "12345", isVisible = false)
    private val secondRank = RankEntity(id = 2, noteId = mutableListOf(3), position = 1, name = "23456")
    private val thirdRank = RankEntity(id = 3, position = 2, name = "34567", isVisible = false)

    private val firstNote = NoteEntity(id = 1, create = DATE_1, change = DATE_2, text = "12345", rankId = 1, rankPs = 0, isStatus = false)
    private val secondNote = NoteEntity(id = 2, create = DATE_1, change = DATE_2, text = "23456", rankId = 1, rankPs = 0, isStatus = true)
    private val thirdNote = NoteEntity(id = 3, create = DATE_3, change = DATE_2, text = "34567", rankId = 2, rankPs = 1, isStatus = false)

    private val thirdAlarm = AlarmEntity(id = 1, noteId = 3, date = DATE_1)

}