package sgtmelon.scriptum.domain.interactor.impl.note

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteBridge
import kotlin.random.Random

/**
 * Test for [RollNoteInteractor].
 */
@ExperimentalCoroutinesApi
class RollNoteInteractorTest : ParentInteractorTest() {

    // TODO many items are common with [TextNoteInteractor]

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IParentNoteBridge

    private val interactor by lazy {
        RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Before override fun setup() {
        super.setup()
        assertNull(interactor.rankIdVisibleList)
    }

    @After override fun tearDown() {
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

        coEvery { rankRepo.getIdVisibleList() } returns emptyList()

        assertEquals(list, interactor.getRankIdVisibleList())

        coVerifySequence {
            rankRepo.getIdVisibleList()
        }
    }

    @Test fun getSaveModel() = FastTest.Interactor.getSaveModel(preferenceRepo) {
        interactor.getSaveModel()
    }

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

    @Test fun getRankDialogItemArray() = startCoTest {
        FastTest.Interactor.getRankDialogItemArray(rankRepo) {
            interactor.getRankDialogItemArray(it)
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
        FastTest.Interactor.getRankId(rankRepo) { interactor.getRankId(it) }
    }

    @Test fun getDateList() = startCoTest {
        val size = getRandomSize()
        val itemList = MutableList<NotificationItem>(size) { mockk() }
        val alarmList = List<NotificationItem.Alarm>(size) { mockk() }
        val dateList = List(size) { nextString() }

        coEvery { alarmRepo.getList() } returns itemList

        for ((i, item) in itemList.withIndex()) {
            every { item.alarm } returns alarmList[i]
            every { alarmList[i].date } returns dateList[i]
        }

        assertEquals(dateList, interactor.getDateList())

        coVerifySequence {
            alarmRepo.getList()

            for ((i, item) in itemList.withIndex()) {
                item.alarm
                alarmList[i].date
            }
        }
    }

    @Test fun clearDate() = startCoTest {
        FastTest.Interactor.clearDate<NoteItem.Roll>(alarmRepo, callback) {
            interactor.clearDate(it)
        }
    }

    @Test fun setDate() = startCoTest {
        FastTest.Interactor.setDate<NoteItem.Roll>(alarmRepo, callback) { item, calendar ->
            interactor.setDate(item, calendar)
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