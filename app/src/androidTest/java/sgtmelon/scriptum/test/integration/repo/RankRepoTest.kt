package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.repository.rank.IRankRepo
import sgtmelon.scriptum.repository.rank.RankRepo
import sgtmelon.scriptum.room.converter.NoteConverter
import sgtmelon.scriptum.room.converter.RankConverter
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [RankRepo]
 */
@RunWith(AndroidJUnit4::class)
class RankRepoTest : ParentIntegrationTest()  {

    private val iRankRepo: IRankRepo = RankRepo(context)

    private val rankConverter = RankConverter()
    private val noteConverter = NoteConverter()

    @Test fun isEmpty() = inRoom {
        assertTrue(iRankRepo.isEmpty())
        iRankRepo.insert(data.uniqueString)
        assertFalse(iRankRepo.isEmpty())
    }

    @Test fun getList() = inRoom {
        val list = mutableListOf<RankItem>()

        assertEquals(list, iRankRepo.getList())

        listOf(rankFirst, rankSecond, rankThird).forEach {
            assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(it))
            list.add(rankConverter.toItem(it))

            assertEquals(list, iRankRepo.getList())
        }
    }

    @Test fun getIdVisibleList() = inRoom {
        val idList = listOf(rankFirst, rankSecond, rankThird).apply {
            forEach { iRankDao.insert(it) }
        }.filter { it.isVisible }.map { it.id }

        assertEquals(idList, iRankRepo.getIdVisibleList())
    }


    @Test fun insertWithUnique() = inRoom {
        val name = data.uniqueString

        assertNotEquals(UNIQUE_ERROR_ID, iRankRepo.insert(name))
        assertEquals(UNIQUE_ERROR_ID, iRankRepo.insert(name))
    }

    @Test fun delete() = inRoom {
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteFirst))
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteSecond))
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankFirst))

        iRankRepo.delete(rankConverter.toItem(rankFirst))

        assertEquals(noteFirst.copy(
                rankId = DbData.Note.Default.RANK_ID,
                rankPs = DbData.Note.Default.RANK_PS
        ), iNoteDao[noteFirst.id])

        assertEquals(noteSecond.copy(
                rankId = DbData.Note.Default.RANK_ID,
                rankPs = DbData.Note.Default.RANK_PS
        ), iNoteDao[noteSecond.id])

        assertTrue(iRankRepo.getList().isEmpty())
    }

    @Test fun updateItem() = inRoom {
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankFirst))

        assertEquals(listOf(rankConverter.toItem(rankFirst)), iRankRepo.getList())

        val rankItem = rankConverter.toItem(rankFirst.copy(name = "54321", isVisible = true))
        iRankRepo.update(rankItem)

        assertEquals(listOf(rankItem), iRankRepo.getList())
    }

    @Test fun updateList() = inRoom {
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankFirst))
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankSecond))

        assertEquals(rankConverter.toItem(listOf(rankFirst, rankSecond)), iRankRepo.getList())

        val rankList = listOf(
                rankConverter.toItem(rankFirst.copy(name = "54321", isVisible = true)),
                rankConverter.toItem(rankSecond.copy(name = "98765", isVisible = false))
        )
        iRankRepo.update(rankList)

        assertEquals(rankList, iRankRepo.getList())
    }


    @Test fun updatePosition() = inRoom {
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteFirst))
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteSecond))
        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteThird))

        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankFirst))
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankSecond))
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankThird))

        val rankList = rankConverter.toItem(listOf(rankThird, rankFirst, rankSecond))
        iRankRepo.updatePosition(rankList)

        assertEquals(0, rankList[0].position)
        assertEquals(1, rankList[1].position)
        assertEquals(2, rankList[2].position)

        assertEquals(1, iNoteDao[noteFirst.id]?.rankPs)
        assertEquals(1, iNoteDao[noteSecond.id]?.rankPs)
        assertEquals(2, iNoteDao[noteThird.id]?.rankPs)

        assertEquals(rankList, rankConverter.toItem(iRankDao.get()))
    }


    @Test fun updateConnection() = inRoom {
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankFirst))
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankSecond))
        assertNotEquals(UNIQUE_ERROR_ID, iRankDao.insert(rankThird))

        assertNotEquals(UNIQUE_ERROR_ID, iNoteDao.insert(noteFirst))

        val noteItem = noteConverter.toItem(noteFirst.copy(rankId = 3, rankPs = 2))
        iRankRepo.updateConnection(noteItem)

        assertEquals(rankThird.copy(noteId = mutableListOf(noteItem.id)), iRankDao[rankThird.id])

        noteItem.clearRank()
        iRankRepo.updateConnection(noteItem)

        assertEquals(rankThird, iRankDao[rankThird.id])
    }


    @Test fun getDialogItemArray() = inRoom {
        val nameList = listOf(rankFirst, rankSecond, rankThird).apply {
            forEach { iRankDao.insert(it) }
        }.map { it.name }.toMutableList().apply { add(0, context.getString(R.string.dialog_item_rank)) }

        assertEquals(nameList, iRankRepo.getDialogItemArray().toList())
    }

    @Test fun getId() = inRoom {
        listOf(rankFirst, rankSecond, rankThird).forEach { iRankDao.insert(it) }

        assertEquals(DbData.Note.Default.RANK_ID, iRankRepo.getId(DbData.Note.Default.RANK_PS))
        assertEquals(rankFirst.id, iRankRepo.getId(check = 0))
        assertEquals(rankSecond.id, iRankRepo.getId(check = 1))
        assertEquals(rankThird.id, iRankRepo.getId(check = 2))
    }

    private companion object {
        val rankFirst = RankEntity(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "12345", isVisible = false)
        val rankSecond = RankEntity(id = 2, noteId = mutableListOf(3), position = 1, name = "23456")
        val rankThird = RankEntity(id = 3, position = 2, name = "34567", isVisible = false)

        val noteFirst = NoteEntity(id = 1, create = DATE_1, change = DATE_2, text = "12345", rankId = 1, rankPs = 0)
        val noteSecond = NoteEntity(id = 2, create = DATE_1, change = DATE_2, text = "23456", rankId = 1, rankPs = 0)
        val noteThird = NoteEntity(id = 3, create = DATE_3, change = DATE_2, text = "34567", rankId = 2, rankPs = 1)
    }

}