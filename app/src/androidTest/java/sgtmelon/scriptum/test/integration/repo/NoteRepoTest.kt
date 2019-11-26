package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.room.converter.NoteConverter
import sgtmelon.scriptum.room.converter.RollConverter
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [NoteRepo]
 */
@RunWith(AndroidJUnit4::class)
class NoteRepoTest : ParentIntegrationTest()  {

    private val iNoteRepo: INoteRepo = NoteRepo(context)

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    @Test fun getListSort() {
        TODO(reason = "#TEST write test")
    }

    @Test fun getItem() {
        TODO(reason = "#TEST write test")
    }

    @Test fun getRollList() = inRoom {
        assertEquals(listOf<RankItem>(), iNoteRepo.getRollList(Random.nextLong()))

        iNoteDao.insert(noteFirst)
        iNoteDao.insert(noteFourth)
        rollListFirst.forEach { iRollDao.insert(it) }
        rollListFourth.forEach { iRollDao.insert(it) }

        assertEquals(rollConverter.toItem(rollListFirst), iNoteRepo.getRollList(noteFirst.id))
        assertEquals(rollConverter.toItem(rollListFourth), iNoteRepo.getRollList(noteFourth.id))
    }


    @Test fun isListHide() = inRoom {
        iRankDao.insert(rankFirst)
        iRankDao.insert(rankSecond.copy(isVisible = false))

        iNoteDao.insert(noteFirst)
        assertFalse(iNoteRepo.isListHide())

        iNoteDao.insert(noteFourth)
        assertFalse(iNoteRepo.isListHide())
    }

    @Test fun clearBin() = inRoom {
        runBlocking {
            launch {
                iNoteDao.insert(noteFirst)
                iNoteDao.insert(noteSecond)
                iNoteDao.insert(noteThird)

                iRankDao.insert(rankFirst)
                iRankDao.insert(rankSecond)

                val itemSecond = noteConverter.toItem(noteSecond)
                val itemThird = noteConverter.toItem(noteThird)

                iNoteRepo.clearBin()

                assertEquals(noteFirst, iNoteDao[noteFirst.id])

                assertEquals(rankFirst.copy(noteId = arrayListOf(4)), iRankDao[rankFirst.id])
                assertNull(iNoteDao[itemSecond.id])

                assertEquals(rankSecond.copy(noteId = arrayListOf()), iRankDao[rankSecond.id])
                assertNull(iNoteDao[itemThird.id])
            }
        }
    }


    @Test fun deleteNote() = inRoom {
        runBlocking {
            launch {
                iNoteDao.insert(noteFirst)
                iAlarmDao.insert(alarmFirst)

                val item = noteConverter.toItem(noteFirst)

                iNoteRepo.deleteNote(item)

                assertChangeTime(item)
                assertTrue(item.isBin)
                assertFalse(item.isStatus)

                assertEquals(noteConverter.toEntity(item), iNoteDao[item.id])
                assertNull(iAlarmDao[item.id])
            }
        }
    }

    @Test fun restoreNote() = inRoom {
        runBlocking {
            launch {
                iNoteDao.insert(noteSecond)

                val item = noteConverter.toItem(noteSecond)

                iNoteRepo.restoreNote(item)

                assertChangeTime(item)
                assertFalse(item.isBin)

                assertEquals(noteConverter.toEntity(item), iNoteDao[item.id])
            }
        }
    }

    @Test fun clearNote() = inRoom {
        runBlocking {
            launch {
                iNoteDao.insert(noteSecond)
                iRankDao.insert(rankFirst)

                val item = noteConverter.toItem(noteSecond)

                iNoteRepo.clearNote(item)

                assertEquals(rankFirst.copy(noteId = arrayListOf(4)), iRankDao[rankFirst.id])
                assertNull(iNoteDao[item.id])
            }
        }
    }


    @Test fun convertToRoll() {
        TODO(reason = "#TEST write test")
    }

    @Test fun convertToText() {
        TODO(reason = "#TEST write test")
    }

    @Test fun getCopyText() = inRoom {
        iNoteDao.insert(noteFirst)
        iNoteDao.insert(noteSecond)
        iNoteDao.insert(noteThird)
        iNoteDao.insert(noteFourth)

        rollListFirst.forEach { iRollDao.insert(it) }
        rollListFourth.forEach { iRollDao.insert(it) }

        val listFirst = rollConverter.toItem(rollListFirst)
        val listFourth = rollConverter.toItem(rollListFourth)

        val itemFirst = noteConverter.toItem(noteFirst, listFirst)
        val itemSecond = noteConverter.toItem(noteSecond)
        val itemThird = noteConverter.toItem(noteThird)
        val itemFourth = noteConverter.toItem(noteFourth, listFourth)

        val textFirst = "${noteFirst.name}\n${listFirst.getText()}"
        val textSecond = noteSecond.text
        val textThird = with(noteThird) { "$name\n$text" }
        val textFourth = "${noteFourth.name}\n${listFourth.getText()}"

        runBlocking {
            launch {
                assertEquals(textFirst, iNoteRepo.getCopyText(itemFirst))
                assertEquals(textSecond, iNoteRepo.getCopyText(itemSecond))
                assertEquals(textThird, iNoteRepo.getCopyText(itemThird))
                assertEquals(textFourth, iNoteRepo.getCopyText(itemFourth))
            }
        }
    }

    @Test fun saveTextNote() {
        TODO(reason = "#TEST write test")
    }

    @Test fun saveRollNote() {
        TODO(reason = "#TEST write test")
    }

    @Test fun updateRollCheckSingle() = inRoom {
        iNoteDao.insert(noteFirst)
        rollListFirst.forEach { iRollDao.insert(it) }

        val list = rollConverter.toItem(rollListFirst)
        val item = noteConverter.toItem(noteFirst, list)

        iNoteRepo.updateRollCheck(item, p = 0)

        assertFalse(list[0].isCheck)
        assertChangeTime(item)
        assertEquals("2/5", item.text)

        assertEquals(list, iNoteRepo.getRollList(item.id))
    }

    @Test fun updateRollCheckAllFalse() = inRoom {
        iNoteDao.insert(noteFirst)
        rollListFirst.forEach { iRollDao.insert(it) }

        val list = rollConverter.toItem(rollListFirst)
        val item = noteConverter.toItem(noteFirst, list)

        iNoteRepo.updateRollCheck(item, check = false)

        assertChangeTime(item)
        assertFalse(list.any { it.isCheck })
        assertEquals(list, iNoteRepo.getRollList(item.id))
    }

    @Test fun updateRollCheckAllTrue() = inRoom {
        iNoteDao.insert(noteFourth)
        rollListFourth.forEach { iRollDao.insert(it) }

        val list = rollConverter.toItem(rollListFourth)
        val item = noteConverter.toItem(noteFourth, list)

        iNoteRepo.updateRollCheck(item, check = true)

        assertChangeTime(item)
        assertTrue(list.any { it.isCheck })
        assertEquals(list, iNoteRepo.getRollList(item.id))
    }

    @Test fun updateNote() = inRoom {
        val entity = noteFirst.copy()

        iNoteDao.insert(entity)
        iNoteRepo.updateNote(noteConverter.toItem(entity.apply {
            create = DATE_1
            change = DATE_2
        }))

        assertEquals(entity, iNoteDao[entity.id])
    }

    private companion object {
        val alarmFirst = AlarmEntity(id = 1, noteId = 1, date = DATE_4)

        val noteFirst = NoteEntity(id = 1,
                create = DATE_0, change = DATE_3, name = "NAME 1", text = "3/5", color = 0,
                type = NoteType.ROLL, rankId = -1, rankPs = -1, isBin = false, isStatus = true
        )

        val noteSecond = NoteEntity(id = 2,
                create = DATE_1, change = DATE_2, name = "", text = "TEXT 1", color = 1,
                type = NoteType.TEXT, rankId = 1, rankPs = 0, isBin = true, isStatus = false
        )

        val noteThird = NoteEntity(id = 3,
                create = DATE_2, change = DATE_1, name = "NAME 3", text = "TEXT 2", color = 2,
                type = NoteType.TEXT, rankId = 2, rankPs = 1, isBin = true, isStatus = false
        )

        val noteFourth = NoteEntity(id = 4,
                create = DATE_3, change = DATE_0, name = "NAME 4", text = "0/2", color = 3,
                type = NoteType.ROLL, rankId = 1, rankPs = 0, isBin = false, isStatus = true
        )

        val rollListFirst = mutableListOf(
                RollEntity(id = 1, noteId = 1, position = 0, isCheck = true, text = "0"),
                RollEntity(id = 2, noteId = 1, position = 1, isCheck = false, text = "1"),
                RollEntity(id = 3, noteId = 1, position = 2, isCheck = true, text = "2"),
                RollEntity(id = 4, noteId = 1, position = 3, isCheck = false, text = "3"),
                RollEntity(id = 5, noteId = 1, position = 4, isCheck = true, text = "4")
        )

        val rollListFourth = mutableListOf(
                RollEntity(id = 6, noteId = 4, position = 0, isCheck = false, text = "0"),
                RollEntity(id = 7, noteId = 4, position = 1, isCheck = false, text = "1")
        )

        val rankFirst = RankEntity(
                id = 1, noteId = arrayListOf(2, 4), position = 0, name = "0", isVisible = true
        )

        val rankSecond = RankEntity(
                id = 2, noteId = arrayListOf(3), position = 1, name = "1", isVisible = false
        )
    }

}