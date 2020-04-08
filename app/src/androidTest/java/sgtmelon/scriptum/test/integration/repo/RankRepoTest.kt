package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.R
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

/**
 * Integration test for [RankRepo]
 */
@RunWith(AndroidJUnit4::class)
class RankRepoTest : ParentIntegrationTest()  {

    private val rankRepo: IRankRepo = RankRepo(context)

    private val rankConverter = RankConverter()
    private val noteConverter = NoteConverter()


    @Test fun getCount() = inRoomTest {
        assertTrue(rankRepo.getCount() == 0)
        rankRepo.insert(data.uniqueString)
        assertFalse(rankRepo.getCount() == 0)
    }

    @Test fun getList() = inRoomTest {
        val list = mutableListOf<RankItem>()

        assertEquals(list, rankRepo.getList())

        noteDao.insert(noteFirst)
        noteDao.insert(noteSecond)
        noteDao.insert(noteThird)

        alarmDao.insert(alarmThird)

        listOf(rankFirst, rankSecond, rankThird).forEach {
            assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(it))

            list.add(rankConverter.toItem(it).apply {
                hasBind = noteId.contains(noteSecond.id)
                hasNotification = noteId.contains(alarmThird.noteId)
            })

            assertEquals(list, rankRepo.getList())
        }
    }

    @Test fun getBind() = inRoomTest {
        noteDao.insert(noteFirst)
        noteDao.insert(noteSecond)
        noteDao.insert(noteThird)

        alarmDao.insert(alarmThird)

        listOf(rankFirst, rankSecond, rankThird).forEach {
            rankDao.insert(it)
            assertEquals(it.noteId.contains(noteSecond.id), rankRepo.getBind(it.noteId))
        }
    }

    @Test fun getIdVisibleList() = inRoomTest {
        val idList = listOf(rankFirst, rankSecond, rankThird).apply {
            forEach { rankDao.insert(it) }
        }.filter { it.isVisible }.map { it.id }

        assertEquals(idList, rankRepo.getIdVisibleList())
    }


    @Test fun insertWithUnique() = inRoomTest {
        val name = data.uniqueString

        assertNotEquals(UNIQUE_ERROR_ID, rankRepo.insert(name))
        assertEquals(UNIQUE_ERROR_ID, rankRepo.insert(name))
    }

    @Test fun delete() = inRoomTest {
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteFirst))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteSecond))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankFirst))

        rankRepo.delete(rankConverter.toItem(rankFirst))

        assertEquals(noteFirst.copy(
                rankId = DbData.Note.Default.RANK_ID,
                rankPs = DbData.Note.Default.RANK_PS
        ), noteDao.get(noteFirst.id))

        assertEquals(noteSecond.copy(
                rankId = DbData.Note.Default.RANK_ID,
                rankPs = DbData.Note.Default.RANK_PS
        ), noteDao.get(noteSecond.id))

        assertTrue(rankRepo.getList().isEmpty())
    }

    @Test fun updateItem() = inRoomTest {
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankFirst))

        assertEquals(listOf(rankConverter.toItem(rankFirst)), rankRepo.getList())

        val rankItem = rankConverter.toItem(rankFirst.copy(name = "54321", isVisible = true))
        rankRepo.update(rankItem)

        assertEquals(listOf(rankItem), rankRepo.getList())
    }

    @Test fun updateList() = inRoomTest {
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankFirst))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankSecond))

        assertEquals(rankConverter.toItem(listOf(rankFirst, rankSecond)), rankRepo.getList())

        val rankList = listOf(
                rankConverter.toItem(rankFirst.copy(name = "54321", isVisible = true)),
                rankConverter.toItem(rankSecond.copy(name = "98765", isVisible = false))
        )
        rankRepo.update(rankList)

        assertEquals(rankList, rankRepo.getList())
    }


    @Test fun updatePosition() = inRoomTest {
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteFirst))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteSecond))
        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteThird))

        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankFirst))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankSecond))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankThird))

        val rankList = rankConverter.toItem(listOf(rankThird, rankFirst, rankSecond))
        val noteIdList = rankList.correctPositions()

        rankRepo.updatePosition(rankList, noteIdList)

        assertEquals(0, rankList[0].position)
        assertEquals(1, rankList[1].position)
        assertEquals(2, rankList[2].position)

        assertEquals(1, noteDao.get(noteFirst.id)?.rankPs)
        assertEquals(1, noteDao.get(noteSecond.id)?.rankPs)
        assertEquals(2, noteDao.get(noteThird.id)?.rankPs)

        assertEquals(rankList, rankConverter.toItem(rankDao.get()))
    }


    @Test fun updateConnection() = inRoomTest {
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankFirst))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankSecond))
        assertNotEquals(UNIQUE_ERROR_ID, rankDao.insert(rankThird))

        assertNotEquals(UNIQUE_ERROR_ID, noteDao.insert(noteFirst))

        val noteItem = noteConverter.toItem(noteFirst.copy(rankId = 3, rankPs = 2))
        rankRepo.updateConnection(noteItem)

        assertEquals(rankThird.copy(noteId = mutableListOf(noteItem.id)), rankDao.get(rankThird.id))

        noteItem.clearRank()
        rankRepo.updateConnection(noteItem)

        assertEquals(rankThird, rankDao.get(rankThird.id))
    }


    @Test fun getDialogItemArray() = inRoomTest {
        val nameList = listOf(rankFirst, rankSecond, rankThird).apply {
            forEach { rankDao.insert(it) }
        }.map { it.name }.toMutableList().apply { add(0, context.getString(R.string.dialog_item_rank)) }

        assertEquals(nameList, rankRepo.getDialogItemArray().toList())
    }

    @Test fun getId() = inRoomTest {
        listOf(rankFirst, rankSecond, rankThird).forEach { rankDao.insert(it) }

        assertEquals(DbData.Note.Default.RANK_ID, rankRepo.getId(DbData.Note.Default.RANK_PS))
        assertEquals(rankFirst.id, rankRepo.getId(position = 0))
        assertEquals(rankSecond.id, rankRepo.getId(position = 1))
        assertEquals(rankThird.id, rankRepo.getId(position = 2))
    }

    private companion object {
        val rankFirst = RankEntity(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "12345", isVisible = false)
        val rankSecond = RankEntity(id = 2, noteId = mutableListOf(3), position = 1, name = "23456")
        val rankThird = RankEntity(id = 3, position = 2, name = "34567", isVisible = false)

        val noteFirst = NoteEntity(id = 1, create = DATE_1, change = DATE_2, text = "12345", rankId = 1, rankPs = 0, isStatus = false)
        val noteSecond = NoteEntity(id = 2, create = DATE_1, change = DATE_2, text = "23456", rankId = 1, rankPs = 0, isStatus = true)
        val noteThird = NoteEntity(id = 3, create = DATE_3, change = DATE_2, text = "34567", rankId = 2, rankPs = 1, isStatus = false)

        val alarmThird = AlarmEntity(id = 1, noteId = 3, date = DATE_1)
    }

}