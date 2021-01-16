package sgtmelon.scriptum.domain.interactor.impl.note

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getText
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [RollNoteInteractor].
 */
@ExperimentalCoroutinesApi
class RollNoteInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IParentNoteBridge

    private val interactor by lazy {
        RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    override fun setUp() {
        super.setUp()

        assertNull(interactor.rankIdVisibleList)
    }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getRankIdVisibleList() = startCoTest {
        val list = mockk<List<Long>>()

        coEvery { rankRepo.getIdVisibleList() } returns list

        assertEquals(list, interactor.getRankIdVisibleList())
        assertEquals(list, interactor.rankIdVisibleList)
        assertEquals(list, interactor.getRankIdVisibleList())

        coVerifySequence {
            rankRepo.getIdVisibleList()
        }
    }

    @Test fun getSaveModel() = FastTest.Note.Interactor.getSaveModel(preferenceRepo) {
        interactor.getSaveModel()
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }


    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()
        val wrongItem = mockk<NoteItem.Text>()
        val item = mockk<NoteItem.Roll>()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        coEvery { noteRepo.getItem(id, isOptimal = false) } returns null
        assertNull(interactor.getItem(id))

        coEvery { noteRepo.getItem(id, isOptimal = false) } returns wrongItem
        assertNull(interactor.getItem(id))

        coEvery { noteRepo.getItem(id, isOptimal = false) } returns item
        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        assertEquals(item, spyInteractor.getItem(id))

        coVerifySequence {
            noteRepo.getItem(id, isOptimal = false)
            noteRepo.getItem(id, isOptimal = false)

            spyInteractor.getItem(id)
            noteRepo.getItem(id, isOptimal = false)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(item, rankIdList, sort)
        }
    }

    /**
     * Can't mockk Arrays. Don't try.
     */
    @Test fun getRankDialogItemArray() = startCoTest {
        val emptyName = nextString()
        val itemArray = Array(size = 5) { nextString() }

        coEvery { rankRepo.getDialogItemArray(emptyName) } returns itemArray
        assertArrayEquals(itemArray, interactor.getRankDialogItemArray(emptyName))

        coVerifySequence {
            rankRepo.getDialogItemArray(emptyName)
        }
    }


    @Test fun setVisible() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        interactor.setVisible(item, updateBind = false)

        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        spyInteractor.setVisible(item, updateBind = true)

        coVerifySequence {
            noteRepo.setRollVisible(item)

            spyInteractor.setVisible(item, updateBind = true)
            noteRepo.setRollVisible(item)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(item, rankIdList, sort)
        }
    }


    @Test fun updateRollCheck_byPosition() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val p = Random.nextInt()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        spyInteractor.updateRollCheck(item, p)

        coVerifySequence {
            spyInteractor.updateRollCheck(item, p)
            noteRepo.updateRollCheck(item, p)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(item, rankIdList, sort)
        }
    }

    @Test fun updateRollCheck_byCheck() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val isCheck = Random.nextBoolean()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        spyInteractor.updateRollCheck(item, isCheck)

        coVerifySequence {
            spyInteractor.updateRollCheck(item, isCheck)
            noteRepo.updateRollCheck(item, isCheck)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(item, rankIdList, sort)
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
        val item = mockk<NoteItem.Roll>()
        val id = Random.nextLong()

        every { item.id } returns id

        interactor.clearDate(item)

        coVerifySequence {
            item.id
            alarmRepo.delete(id)
            item.id
            callback.cancelAlarm(id)
        }
    }

    @Test fun setDate() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val id = Random.nextLong()
        val calendar = mockk<Calendar>()
        val date = nextString()

        FastMock.timeExtension()
        every { calendar.getText() } returns date
        every { item.id } returns id

        interactor.setDate(item, calendar)

        coVerifySequence {
            calendar.getText()
            alarmRepo.insertOrUpdate(item, date)
            item.id
            callback.setAlarm(calendar, id)
        }
    }

    @Test fun convertNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        coEvery { noteRepo.convertNote(item, useCache = true) } returns mockk()

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item, useCache = true)
        }
    }


    @Test fun restoreNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        interactor.restoreNote(item)

        coVerifySequence {
            noteRepo.restoreNote(item)
        }
    }

    @Test fun updateNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        interactor.updateNote(item, updateBind = false)

        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        spyInteractor.updateNote(item, updateBind = true)

        coVerifySequence {
            noteRepo.updateNote(item)

            spyInteractor.updateNote(item, updateBind = true)
            noteRepo.updateNote(item)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(item, rankIdList, sort)
        }
    }

    @Test fun clearNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        interactor.clearNote(item)

        coVerifySequence {
            noteRepo.clearNote(item)
        }
    }

    @Test fun saveNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val isCreate = Random.nextBoolean()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        spyInteractor.saveNote(item, isCreate)

        coVerifySequence {
            spyInteractor.saveNote(item, isCreate)
            noteRepo.saveNote(item, isCreate)
            rankRepo.updateConnection(item)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(item, rankIdList, sort)
        }
    }

    @Test fun deleteNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val id = Random.nextLong()

        every { item.id } returns id

        interactor.deleteNote(item)

        coVerifySequence {
            noteRepo.deleteNote(item)

            item.id
            callback.cancelAlarm(id)
            item.id
            callback.cancelNoteBind(id)
        }
    }
}