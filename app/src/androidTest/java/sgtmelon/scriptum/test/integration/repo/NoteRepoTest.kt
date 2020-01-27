package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.NoteRepo
import sgtmelon.scriptum.repository.room.NoteRepo.Companion.onConvertRoll
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

    private val noteRepo: INoteRepo = NoteRepo(context)

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    @Test fun getCount() = inRoomTest {
        assertEquals(0, noteRepo.getCount(bin = false))
        assertEquals(0, noteRepo.getCount(bin = true))

        noteDao.insert(noteFirst)
        noteDao.insert(noteSecond)
        noteDao.insert(noteThird)
        noteDao.insert(noteFourth)

        rankDao.insert(rankFirst)
        rankDao.insert(rankSecond)

        assertEquals(2, noteRepo.getCount(bin = false))
        assertEquals(2, noteRepo.getCount(bin = true))

        rankDao.update(rankFirst.copy(isVisible = false))

        assertEquals(1, noteRepo.getCount(bin = false))
    }

    @Test fun getList() = inRoomTest {
        noteRepo.getList(0, true, false, true)
        TODO(reason = "#TEST write test")
    }

    @Test fun getItem() = inRoomTest {
        val rollList = rollConverter.toItem(rollListFirst)
        val noteItem = noteConverter.toItem(noteFirst, rollList, alarmFirst)

        val rollOptimalList = rollList.subList(0, NoteItem.ROLL_OPTIMAL_SIZE)
        val noteOptimalItem = noteConverter.toItem(noteFirst, rollOptimalList, alarmFirst)

        assertNull(noteRepo.getItem(noteItem.id, optimisation = true))

        noteDao.insert(noteFirst)
        rollListFirst.forEach { rollDao.insert(it) }
        alarmDao.insert(alarmFirst)

        assertEquals(noteItem, noteRepo.getItem(noteItem.id, optimisation = false))
        assertEquals(noteOptimalItem, noteRepo.getItem(noteItem.id, optimisation = true))
    }

    @Test fun getRollList() = inRoomTest {
        assertEquals(listOf<RankItem>(), noteRepo.getRollList(Random.nextLong()))

        noteDao.insert(noteFirst)
        noteDao.insert(noteFourth)
        rollListFirst.forEach { rollDao.insert(it) }
        rollListFourth.forEach { rollDao.insert(it) }

        assertEquals(rollConverter.toItem(rollListFirst), noteRepo.getRollList(noteFirst.id))
        assertEquals(rollConverter.toItem(rollListFourth), noteRepo.getRollList(noteFourth.id))
    }


    @Test fun isListHide() = inRoomTest {
        rankDao.insert(rankFirst)
        rankDao.insert(rankSecond.copy(isVisible = false))

        noteDao.insert(noteFirst)
        assertFalse(noteRepo.isListHide())

        noteDao.insert(noteFourth)
        assertFalse(noteRepo.isListHide())
    }

    @Test fun clearBin() = inRoomTest {
        noteDao.insert(noteFirst)
        noteDao.insert(noteSecond)
        noteDao.insert(noteThird)

        rankDao.insert(rankFirst)
        rankDao.insert(rankSecond)

        val itemSecond = noteConverter.toItem(noteSecond)
        val itemThird = noteConverter.toItem(noteThird)

        noteRepo.clearBin()

        assertEquals(noteFirst, noteDao.get(noteFirst.id))

        assertEquals(rankFirst.copy(noteId = arrayListOf(4)), rankDao.get(rankFirst.id))
        assertNull(noteDao.get(itemSecond.id))

        assertEquals(rankSecond.copy(noteId = arrayListOf()), rankDao.get(rankSecond.id))
        assertNull(noteDao.get(itemThird.id))
    }


    @Test fun deleteNote() = inRoomTest {
        noteDao.insert(noteFirst)
        alarmDao.insert(alarmFirst)

        val item = noteConverter.toItem(noteFirst)

        noteRepo.deleteNote(item)

        assertChangeTime(item)
        assertTrue(item.isBin)
        assertFalse(item.isStatus)

        assertEquals(noteConverter.toEntity(item), noteDao.get(item.id))
        assertNull(alarmDao.get(item.id))
    }

    @Test fun restoreNote() = inRoomTest {
        noteDao.insert(noteSecond)

        val item = noteConverter.toItem(noteSecond)

        noteRepo.restoreNote(item)

        assertChangeTime(item)
        assertFalse(item.isBin)

        assertEquals(noteConverter.toEntity(item), noteDao.get(item.id))
    }

    @Test fun clearNote() = inRoomTest {
        noteDao.insert(noteSecond)
        rankDao.insert(rankFirst)

        val item = noteConverter.toItem(noteSecond)

        noteRepo.clearNote(item)

        assertEquals(rankFirst.copy(noteId = arrayListOf(4)), rankDao.get(rankFirst.id))
        assertNull(noteDao.get(item.id))
    }

    @Test fun convertToRoll() = inRoomTest {
        noteRepo.convertToRoll(NoteItem(0, "", "", color = 0, type = NoteType.TEXT))
        TODO(reason = "#TEST write test")
    }

    @Test fun convertToText() = inRoomTest {
        val rollList = rollConverter.toItem(rollListFirst)
        val noteItem = noteConverter.toItem(noteFirst, rollList, alarmFirst)

        noteDao.insert(noteFirst)
        rollListFirst.forEach { rollDao.insert(it) }
        alarmDao.insert(alarmFirst)

        noteRepo.convertToText(noteItem.deepCopy(), useCache = false)
        noteItem.onConvertRoll()

        assertEquals(noteItem, noteRepo.getItem(noteItem.id, optimisation = false))
    }

    @Test fun convertToTextUseCache() = inRoomTest {
        val rollList = rollConverter.toItem(rollListFirst)
        val noteItem = noteConverter.toItem(noteFirst, rollList, alarmFirst)

        noteDao.insert(noteFirst)
        rollListFirst.forEach { rollDao.insert(it) }
        alarmDao.insert(alarmFirst)

        noteRepo.convertToText(noteItem.deepCopy(), useCache = true)
        noteItem.onConvertRoll()

        assertEquals(noteItem, noteRepo.getItem(noteItem.id, optimisation = false))
    }

    @Test fun getCopyText() = inRoomTest {
        noteDao.insert(noteFirst)
        noteDao.insert(noteSecond)
        noteDao.insert(noteThird)
        noteDao.insert(noteFourth)

        rollListFirst.forEach { rollDao.insert(it) }
        rollListFourth.forEach { rollDao.insert(it) }

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

        assertEquals(textFirst, noteRepo.getCopyText(itemFirst))
        assertEquals(textSecond, noteRepo.getCopyText(itemSecond))
        assertEquals(textThird, noteRepo.getCopyText(itemThird))
        assertEquals(textFourth, noteRepo.getCopyText(itemFourth))
    }

    @Test fun saveTextNote() = inRoomTest {
        noteRepo.saveTextNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT), true)
        TODO(reason = "#TEST write test")
    }

    @Test fun saveRollNote() = inRoomTest {
        noteRepo.saveRollNote(NoteItem(0, "", "", color = 0, type = NoteType.TEXT), true)
        TODO(reason = "#TEST write test")
    }

    @Test fun updateRollCheckSingle() = inRoomTest {
        noteDao.insert(noteFirst)
        rollListFirst.forEach { rollDao.insert(it) }

        val list = rollConverter.toItem(rollListFirst)
        val item = noteConverter.toItem(noteFirst, list)

        item.onItemCheck(p = 0)

        noteRepo.updateRollCheck(item, p = 0)

        assertEquals(item, noteRepo.getItem(item.id, optimisation = false))
        assertEquals(list, noteRepo.getRollList(item.id))
    }

    @Test fun updateRollCheckAllFalse() = inRoomTest {
        noteDao.insert(noteFirst)
        rollListFirst.forEach { rollDao.insert(it) }

        val list = rollConverter.toItem(rollListFirst)
        val item = noteConverter.toItem(noteFirst, list)

        list.forEach { it.isCheck = true }

        val check = item.onItemLongCheck()

        noteRepo.updateRollCheck(item, check)

        assertEquals(item, noteRepo.getItem(item.id, optimisation = false))
        assertEquals(list, noteRepo.getRollList(item.id))
    }

    @Test fun updateRollCheckAllTrue() = inRoomTest {
        noteDao.insert(noteFourth)
        rollListFourth.forEach { rollDao.insert(it) }

        val list = rollConverter.toItem(rollListFourth)
        val item = noteConverter.toItem(noteFourth, list)

        list.forEach { it.isCheck = true }
        list.random().isCheck = false

        val check = item.onItemLongCheck()

        noteRepo.updateRollCheck(item, check)

        assertEquals(item, noteRepo.getItem(item.id, optimisation = false))
        assertEquals(list, noteRepo.getRollList(item.id))
    }

    @Test fun updateNote() = inRoomTest {
        val entity = noteFirst.copy()

        noteDao.insert(entity)
        noteRepo.updateNote(noteConverter.toItem(entity.apply {
            create = DATE_1
            change = DATE_2
        }))

        assertEquals(entity, noteDao.get(entity.id))
    }

    private companion object {
        val alarmFirst = AlarmEntity(id = 1, noteId = 1, date = DATE_4)

        val noteFirst = NoteEntity(id = 1,
                create = DATE_5, change = DATE_3, name = "NAME 1", text = "3/5", color = 0,
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
                create = DATE_3, change = DATE_5, name = "NAME 4", text = "0/2", color = 3,
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