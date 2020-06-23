package sgtmelon.scriptum.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getText
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteBridge
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
    @MockK lateinit var callback: IParentNoteBridge

    private val interactor by lazy {
        RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getSaveModel() = FastTest.Note.Interactor.getSaveModel(preferenceRepo) {
        interactor.getSaveModel()
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }


    @Test fun getItem() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val sort = TestData.sort

        val wrongItem = data.firstNote.deepCopy()
        val firstItem = data.secondNote.deepCopy()
        val secondItem = data.fourthNote.deepCopy()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        coEvery { noteRepo.getItem(wrongItem.id, isOptimal = false) } returns null
        assertNull(interactor.getItem(wrongItem.id))

        coEvery { noteRepo.getItem(wrongItem.id, isOptimal = false) } returns wrongItem
        assertNull(interactor.getItem(wrongItem.id))

        coEvery { noteRepo.getItem(firstItem.id, isOptimal = false) } returns firstItem
        assertEquals(firstItem, interactor.getItem(firstItem.id))

        coEvery { noteRepo.getItem(secondItem.id, isOptimal = false) } returns secondItem
        assertEquals(secondItem, interactor.getItem(secondItem.id))

        coVerifySequence {
            noteRepo.getItem(wrongItem.id, isOptimal = false)
            noteRepo.getItem(wrongItem.id, isOptimal = false)

            noteRepo.getItem(firstItem.id, isOptimal = false)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(firstItem, rankIdVisibleList, sort)

            noteRepo.getItem(secondItem.id, isOptimal = false)
            preferenceRepo.sort
            callback.notifyNoteBind(secondItem, rankIdVisibleList, sort)
        }
    }

    @Test fun getRankDialogItemArray() = startCoTest {
        val emptyName = Random.nextString()
        val itemArray = Array(size = 5) { Random.nextString() }

        coEvery { rankRepo.getDialogItemArray(emptyName) } returns itemArray
        assertArrayEquals(itemArray, interactor.getRankDialogItemArray(emptyName))

        coVerifySequence {
            rankRepo.getDialogItemArray(emptyName)
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
        val visibleList = listOf(Random.nextBoolean(), Random.nextBoolean())
        val noteId = Random.nextLong()

        visibleList.forEach {
            coEvery { noteRepo.getRollVisible(noteId) } returns it
            assertEquals(it, interactor.getVisible(noteId))
        }

        coVerifySequence {
            repeat(visibleList.size) {
                noteRepo.getRollVisible(noteId)
            }
        }
    }


    @Test fun updateRollCheck_byPosition() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val sort = TestData.sort

        val firstItem = data.secondNote.deepCopy()
        val secondItem = data.fourthNote.deepCopy()
        val firstPosition = Random.nextInt()
        val secondPosition = Random.nextInt()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateRollCheck(firstItem, firstPosition)
        interactor.updateRollCheck(secondItem, secondPosition)

        coVerifySequence {
            noteRepo.updateRollCheck(firstItem, firstPosition)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(firstItem, rankIdVisibleList, sort)

            noteRepo.updateRollCheck(secondItem, secondPosition)
            preferenceRepo.sort
            callback.notifyNoteBind(secondItem, rankIdVisibleList, sort)
        }
    }

    @Test fun updateRollCheck_byCheck() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val sort = TestData.sort

        val firstItem = data.secondNote.deepCopy()
        val secondItem = data.fourthNote.deepCopy()
        val firstCheck = Random.nextBoolean()
        val secondCheck = Random.nextBoolean()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateRollCheck(firstItem, firstCheck)
        interactor.updateRollCheck(secondItem, secondCheck)

        coVerifySequence {
            noteRepo.updateRollCheck(firstItem, firstCheck)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(firstItem, rankIdVisibleList, sort)

            noteRepo.updateRollCheck(secondItem, secondCheck)
            preferenceRepo.sort
            callback.notifyNoteBind(secondItem, rankIdVisibleList, sort)
        }
    }

    @Test fun getRankId() = startCoTest {
        val list = listOf(
                Pair(Random.nextLong(), Random.nextInt()),
                Pair(Random.nextLong(), Random.nextInt())
        )

        list.forEach {
            coEvery { rankRepo.getId(it.second) } returns it.first
            assertEquals(it.first, interactor.getRankId(it.second))
        }

        coVerifySequence {
            list.forEach { rankRepo.getId(it.second) }
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
        val calendar = getCalendar()

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
        val sort = TestData.sort

        val firstItem = data.secondNote.deepCopy()
        val secondItem = data.fourthNote.deepCopy()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateNote(firstItem, updateBind = true)
        interactor.updateNote(secondItem, updateBind = true)

        interactor.updateNote(firstItem, updateBind = false)

        coVerifySequence {
            noteRepo.updateNote(firstItem)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(firstItem, rankIdVisibleList, sort)

            noteRepo.updateNote(secondItem)
            preferenceRepo.sort
            callback.notifyNoteBind(secondItem, rankIdVisibleList, sort)

            noteRepo.updateNote(firstItem)
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
        val sort = TestData.sort

        val firstItem = data.secondNote.deepCopy()
        val secondItem = data.fourthNote.deepCopy()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.saveNote(firstItem, isCreate = true)
        interactor.saveNote(secondItem, isCreate = false)

        coVerifySequence {
            noteRepo.saveNote(firstItem, isCreate = true)
            rankRepo.updateConnection(firstItem)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(firstItem, rankIdVisibleList, sort)

            noteRepo.saveNote(secondItem, isCreate = false)
            rankRepo.updateConnection(secondItem)
            preferenceRepo.sort
            callback.notifyNoteBind(secondItem, rankIdVisibleList, sort)
        }
    }

    @Test fun deleteNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Roll>().random()

        interactor.deleteNote(item)

        coVerifySequence {
            noteRepo.deleteNote(item)

            callback.cancelAlarm(item.id)
            callback.cancelNoteBind(item.id)
        }
    }

}