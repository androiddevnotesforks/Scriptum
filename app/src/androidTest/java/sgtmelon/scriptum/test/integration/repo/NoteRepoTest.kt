package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.room.converter.model.NoteConverter
import sgtmelon.scriptum.room.converter.model.RollConverter
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.room.entity.RankEntity
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel.Companion.onItemCheck
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel.Companion.onItemLongCheck

/**
 * Integration test for [NoteRepo]
 */
@RunWith(AndroidJUnit4::class)
class NoteRepoTest : ParentIntegrationTest()  {

    private val iNoteRepo: INoteRepo = NoteRepo(context)

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    @Test fun getCount() = inRoomTest {
        assertEquals(0, iNoteRepo.getCount(bin = false))
        assertEquals(0, iNoteRepo.getCount(bin = true))

        iNoteDao.insert(noteFirst)
        iNoteDao.insert(noteSecond)
        iNoteDao.insert(noteThird)
        iNoteDao.insert(noteFourth)

        iRankDao.insert(rankFirst)
        iRankDao.insert(rankSecond)

        assertEquals(2, iNoteRepo.getCount(bin = false))
        assertEquals(2, iNoteRepo.getCount(bin = true))

        iRankDao.update(rankFirst.copy(isVisible = false))

        assertEquals(1, iNoteRepo.getCount(bin = false))
    }

    @Test fun getList() = inRoomTest {
        iNoteRepo.getList(0, true, false, true)
        TODO(reason = "#TEST write test")
    }

    @Test fun getItem() = inRoomTest {
        val rollList = rollConverter.toItem(rollListFirst)
        val noteItem = noteConverter.toItem(noteFirst, rollList, alarmFirst)

        val rollOptimalList = rollList.subList(0, NoteItem.ROLL_OPTIMAL_SIZE)
        val noteOptimalItem = noteConverter.toItem(noteFirst, rollOptimalList, alarmFirst)

        assertNull(iNoteRepo.getItem(noteItem.id, optimisation = true))

        iNoteDao.insert(noteFirst)
        rollListFirst.forEach { iRollDao.insert(it) }
        iAlarmDao.insert(alarmFirst)

        assertEquals(noteItem, iNoteRepo.getItem(noteItem.id, optimisation = false))
        assertEquals(noteOptimalItem, iNoteRepo.getItem(noteItem.id, optimisation = true))
    }

    @Test fun getRollList() = inRoomTest {
        assertEquals(listOf<RankItem>(), iNoteRepo.getRollList(Random.nextLong()))

        iNoteDao.insert(noteFirst)
        iNoteDao.insert(noteFourth)
        rollListFirst.forEach { iRollDao.insert(it) }
        rollListFourth.forEach { iRollDao.insert(it) }

        assertEquals(rollConverter.toItem(rollListFirst), iNoteRepo.getRollList(noteFirst.id))
        assertEquals(rollConverter.toItem(rollListFourth), iNoteRepo.getRollList(noteFourth.id))
    }


    @Test fun isListHide() = inRoomTest {
        iRankDao.insert(rankFirst)
        iRankDao.insert(rankSecond.copy(isVisible = false))

        iNoteDao.insert(noteFirst)
        assertFalse(iNoteRepo.isListHide())

        iNoteDao.insert(noteFourth)
        assertFalse(iNoteRepo.isListHide())
    }

    @Test fun clearBin() = inRoomTest {
        iNoteDao.insert(noteFirst)
        iNoteDao.insert(noteSecond)
        iNoteDao.insert(noteThird)

        iRankDao.insert(rankFirst)
        iRankDao.insert(rankSecond)

        val itemSecond = noteConverter.toItem(noteSecond)
        val itemThird = noteConverter.toItem(noteThird)

        iNoteRepo.clearBin()

        assertEquals(noteFirst, iNoteDao.get(noteFirst.id))

        assertEquals(rankFirst.copy(noteId = arrayListOf(4)), iRankDao.get(rankFirst.id))
        assertNull(iNoteDao.get(itemSecond.id))

        assertEquals(rankSecond.copy(noteId = arrayListOf()), iRankDao.get(rankSecond.id))
        assertNull(iNoteDao.get(itemThird.id))
    }


    @Test fun deleteNote() = inRoomTest {
        iNoteDao.insert(noteFirst)
        iAlarmDao.insert(alarmFirst)

        val item = noteConverter.toItem(noteFirst)

        iNoteRepo.deleteNote(item)

        assertChangeTime(item)
        assertTrue(item.isBin)
        assertFalse(item.isStatus)

        assertEquals(noteConverter.toEntity(item), iNoteDao.get(item.id))
        assertNull(iAlarmDao.get(item.id))
    }

    @Test fun restoreNote() = inRoomTest {
        iNoteDao.insert(noteSecond)

        val item = noteConverter.toItem(noteSecond)

        iNoteRepo.restoreNote(item)

        assertChangeTime(item)
        assertFalse(item.isBin)

        assertEquals(noteConverter.toEntity(item), iNoteDao.get(item.id))
    }

    @Test fun clearNote() = inRoomTest {
        iNoteDao.insert(noteSecond)
        iRankDao.insert(rankFirst)

        val item = noteConverter.toItem(noteSecond)

        iNoteRepo.clearNote(item)

        assertEquals(rankFirst.copy(noteId = arrayListOf(4)), iRankDao.get(rankFirst.id))
        assertNull(iNoteDao.get(item.id))
    }

    @Test fun convertToRoll() = inRoomTest {
        iNoteRepo.convertToRoll(NoteItem(0, "", "", color = 0, type = NoteType.TEXT))
        TODO(reason = "#TEST write test")
    }

    @Test fun convertToText() = inRoomTest {
        iNoteRepo.convertToText(NoteItem(0, "", "", color = 0, type = NoteType.TEXT))
        TODO(reason = "#TEST write test")
    }

    @Test fun getCopyText() = inRoomTest {
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

        assertEquals(textFirst, iNoteRepo.getCopyText(itemFirst))
        assertEquals(textSecond, iNoteRepo.getCopyText(itemSecond))
        assertEquals(textThird, iNoteRepo.getCopyText(itemThird))
        assertEquals(textFourth, iNoteRepo.getCopyText(itemFourth))
    }

    @Test fun saveTextNote() = inRoomTest {
        iNoteRepo.saveTextNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT), true)
        TODO(reason = "#TEST write test")
    }

    @Test fun saveRollNote() = inRoomTest {
        iNoteRepo.saveRollNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT), true)
        TODO(reason = "#TEST write test")
    }

    @Test fun updateRollCheckSingle() = inRoomTest {
        iNoteDao.insert(noteFirst)
        rollListFirst.forEach { iRollDao.insert(it) }

        val list = rollConverter.toItem(rollListFirst)
        val item = noteConverter.toItem(noteFirst, list)

        item.onItemCheck(p = 0)

        iNoteRepo.updateRollCheck(item, p = 0)

        assertEquals(item, iNoteRepo.getItem(item.id, optimisation = false))
        assertEquals(list, iNoteRepo.getRollList(item.id))
    }

    @Test fun updateRollCheckAllFalse() = inRoomTest {
        iNoteDao.insert(noteFirst)
        rollListFirst.forEach { iRollDao.insert(it) }

        val list = rollConverter.toItem(rollListFirst)
        val item = noteConverter.toItem(noteFirst, list)

        list.forEach { it.isCheck = true }

        val check = item.onItemLongCheck()

        iNoteRepo.updateRollCheck(item, check)

        assertEquals(item, iNoteRepo.getItem(item.id, optimisation = false))
        assertEquals(list, iNoteRepo.getRollList(item.id))
    }

    @Test fun updateRollCheckAllTrue() = inRoomTest {
        iNoteDao.insert(noteFourth)
        rollListFourth.forEach { iRollDao.insert(it) }

        val list = rollConverter.toItem(rollListFourth)
        val item = noteConverter.toItem(noteFourth, list)

        list.forEach { it.isCheck = true }
        list.random().isCheck = false

        val check = item.onItemLongCheck()

        iNoteRepo.updateRollCheck(item, check)

        assertEquals(item, iNoteRepo.getItem(item.id, optimisation = false))
        assertEquals(list, iNoteRepo.getRollList(item.id))
    }

    @Test fun updateNote() = inRoomTest {
        val entity = noteFirst.copy()

        iNoteDao.insert(entity)
        iNoteRepo.updateNote(noteConverter.toItem(entity.apply {
            create = DATE_1
            change = DATE_2
        }))

        assertEquals(entity, iNoteDao.get(entity.id))
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