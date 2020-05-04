package sgtmelon.scriptum.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getText
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [RollNoteInteractor].
 */
@ExperimentalCoroutinesApi
class RollNoteInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IRollNoteBridge

    private val interactor by lazy {
        RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getSaveModel() {
        val model = with(Random) { SaveControl.Model(nextBoolean(), nextBoolean(), nextInt()) }

        every { preferenceRepo.pauseSaveOn } returns model.pauseSaveOn
        every { preferenceRepo.autoSaveOn } returns model.autoSaveOn
        every { preferenceRepo.savePeriod } returns model.savePeriod

        assertEquals(model, interactor.getSaveModel())

        verifySequence {
            preferenceRepo.pauseSaveOn
            preferenceRepo.autoSaveOn
            preferenceRepo.savePeriod
        }
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }


    @Test fun getItem() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList

        val wrongItem = data.noteFirst.deepCopy()
        val firstItem = data.noteSecond.deepCopy()
        val secondItem = data.noteFourth.deepCopy()

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        coEvery { noteRepo.getItem(wrongItem.id, optimisation = false) } returns wrongItem
        assertNull(interactor.getItem(wrongItem.id))

        coEvery { noteRepo.getItem(firstItem.id, optimisation = false) } returns firstItem
        assertEquals(firstItem, interactor.getItem(firstItem.id))

        coEvery { noteRepo.getItem(secondItem.id, optimisation = false) } returns secondItem
        assertEquals(secondItem, interactor.getItem(secondItem.id))

        coVerifySequence {
            noteRepo.getItem(wrongItem.id, optimisation = false)

            noteRepo.getItem(firstItem.id, optimisation = false)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(firstItem, rankIdVisibleList)

            noteRepo.getItem(secondItem.id, optimisation = false)
            callback.notifyNoteBind(secondItem, rankIdVisibleList)
        }
    }

    @Test fun getRankDialogItemArray() = startCoTest {
        val itemArray = Array(size = 5) { TestData.uniqueString }

        coEvery { rankRepo.getDialogItemArray() } returns itemArray

        assertArrayEquals(itemArray, interactor.getRankDialogItemArray())

        coVerifySequence {
            rankRepo.getDialogItemArray()
        }
    }


    @Test fun setVisible() = startCoTest {
        val noteId = Random.nextLong()
        val isVisible = Random.nextBoolean()

        interactor.setVisible(noteId, isVisible)

        coVerifySequence {
            noteRepo.setRollVisible(noteId, isVisible)
        }
    }

    @Test fun getVisible() = startCoTest {
        val noteId = Random.nextLong()
        val isVisible = Random.nextBoolean()

        coEvery { noteRepo.getRollVisible(noteId) } returns isVisible

        assertEquals(isVisible, interactor.getVisible(noteId))

        coVerifySequence {
            noteRepo.getRollVisible(noteId)
        }
    }


    @Test fun updateRollCheck_byPosition() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList

        val firstItem = data.noteSecond.deepCopy()
        val secondItem = data.noteFourth.deepCopy()
        val firstPosition = Random.nextInt()
        val secondPosition = Random.nextInt()

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateRollCheck(firstItem, firstPosition)
        interactor.updateRollCheck(secondItem, secondPosition)

        coVerifySequence {
            noteRepo.updateRollCheck(firstItem, firstPosition)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(firstItem, rankIdVisibleList)

            noteRepo.updateRollCheck(secondItem, secondPosition)
            callback.notifyNoteBind(secondItem, rankIdVisibleList)
        }
    }

    @Test fun updateRollCheck_byCheck() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList

        val firstItem = data.noteSecond.deepCopy()
        val secondItem = data.noteFourth.deepCopy()
        val firstCheck = Random.nextBoolean()
        val secondCheck = Random.nextBoolean()

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateRollCheck(firstItem, firstCheck)
        interactor.updateRollCheck(secondItem, secondCheck)

        coVerifySequence {
            noteRepo.updateRollCheck(firstItem, firstCheck)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(firstItem, rankIdVisibleList)

            noteRepo.updateRollCheck(secondItem, secondCheck)
            callback.notifyNoteBind(secondItem, rankIdVisibleList)
        }
    }

    @Test fun getRankId() = startCoTest {
        val check = Random.nextInt()
        val id = Random.nextLong()

        coEvery { rankRepo.getId(check) } returns id

        assertEquals(id, interactor.getRankId(check))

        coVerifySequence {
            rankRepo.getId(check)
        }
    }

    @Test fun getDateList() = startCoTest {
        val itemList = TestData.Notification.itemList
        val dateList = itemList.map { it.alarm.date }

        coEvery { alarmRepo.getList() } returns itemList

        assertEquals(dateList, interactor.getDateList())

        coVerifySequence {
            alarmRepo.getList()
        }
    }

    @Test fun clearDate() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()

        interactor.clearDate(item)

        coVerifySequence {
            alarmRepo.delete(item.id)
            callback.cancelAlarm(item.id)
        }
    }

    @Test fun setDate() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()
        val calendar = Calendar.getInstance()

        interactor.setDate(item, calendar)

        coVerifySequence {
            alarmRepo.insertOrUpdate(item, calendar.getText())
            callback.setAlarm(calendar, item.id)
        }
    }

    @Test fun convertNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()
        val returnItem = data.itemList.filterIsInstance<NoteItem.Text>().random()

        coEvery { noteRepo.convertNote(item, useCache = true) } returns returnItem

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item, useCache = true)
        }
    }


    @Test fun restoreNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()

        interactor.restoreNote(item)

        coVerifySequence {
            noteRepo.restoreNote(item)
        }
    }

    @Test fun updateNote() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val firstItem = data.noteSecond.deepCopy()
        val secondItem = data.noteFourth.deepCopy()

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateNote(firstItem, updateBind = false)
        interactor.updateNote(firstItem, updateBind = true)
        interactor.updateNote(secondItem, updateBind = true)

        coVerifySequence {
            noteRepo.updateNote(firstItem)

            noteRepo.updateNote(firstItem)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(firstItem, rankIdVisibleList)

            noteRepo.updateNote(secondItem)
            callback.notifyNoteBind(secondItem, rankIdVisibleList)
        }
    }

    @Test fun clearNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()

        interactor.clearNote(item)

        coVerifySequence {
            noteRepo.clearNote(item)
        }
    }

    @Test fun saveNote() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val firstItem = data.noteSecond.deepCopy()
        val secondItem = data.noteFourth.deepCopy()

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.saveNote(firstItem, isCreate = false)
        interactor.saveNote(secondItem, isCreate = true)

        coVerifySequence {
            noteRepo.saveNote(firstItem, isCreate = false)
            rankRepo.updateConnection(firstItem)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(firstItem, rankIdVisibleList)

            noteRepo.saveNote(secondItem, isCreate = true)
            rankRepo.updateConnection(secondItem)
            callback.notifyNoteBind(secondItem, rankIdVisibleList)
        }
    }

    @Test fun deleteNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()

        interactor.deleteNote(item)

        coVerifySequence {
            noteRepo.deleteNote(item)

            callback.cancelAlarm(item.id)
            callback.cancelNoteBind(item.id.toInt())
        }
    }

}