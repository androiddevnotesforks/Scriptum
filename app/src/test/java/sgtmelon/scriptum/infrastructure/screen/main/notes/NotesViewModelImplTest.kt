package sgtmelon.scriptum.infrastructure.screen.main.notes

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extensions.isBeforeNow
import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationDateListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNoteListUseCase
import sgtmelon.scriptum.domain.useCase.main.SortNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.DeleteNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.UpdateNoteUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.test.common.nextString

/**
 * Test for [NotesViewModelImpl].
 */
// TODO write tests
@ExperimentalCoroutinesApi
class NotesViewModelImplTest : ParentViewModelTest() {

    //region Setup

    private val data = TestData.Note

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var interactor: INotesInteractor

    @MockK lateinit var getList: GetNoteListUseCase
    @MockK lateinit var sortList: SortNoteListUseCase
    @MockK lateinit var getCopyText: GetCopyTextUseCase
    @MockK lateinit var updateNote: UpdateNoteUseCase
    @MockK lateinit var deleteNote: DeleteNoteUseCase
    @MockK lateinit var setNotification: SetNotificationUseCase
    @MockK lateinit var deleteNotification: DeleteNotificationUseCase
//    @MockK lateinit var getNotification: GetNotificationUseCase
    @MockK lateinit var getNotificationDateList: GetNotificationDateListUseCase

    @MockK lateinit var calendar: Calendar

    private val viewModel by lazy {
        NotesViewModelImpl(
            callback, preferencesRepo, interactor,
            getList, sortList, getCopyText, updateNote, deleteNote, setNotification,
            deleteNotification, getNotificationDateList
        )
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            callback, preferencesRepo, interactor, calendar,
            getList, sortList, getCopyText, updateNote, deleteNote, setNotification,
            deleteNotification, getNotificationDateList
        )
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
            callback.setupDialog()

            callback.prepareForLoad()
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { getList(isBin = false) } returns itemList
        coEvery { interactor.isListHide() } returns isListHide

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            callback.hideEmptyInfo()
            callback.showProgress()
            getList(isBin = false)

            interactor.isListHide()
            callback.notifyList(itemList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<NoteItem>()
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.isListHide() } returns isListHide

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            interactor.isListHide()
            callback.notifyList(itemList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = List<NoteItem>(getRandomSize()) { mockk() }
        val returnList = List<NoteItem>(getRandomSize()) { mockk() }
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { getList(isBin = false) } returns returnList
        coEvery { interactor.isListHide() } returns isListHide

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.notifyList(returnList)
            callback.onBindingList()
            interactor.getCount()
            getList(isBin = false)
            interactor.isListHide()
            callback.notifyList(returnList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }

        assertEquals(returnList, viewModel.itemList)
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        val startList = List<NoteItem>(getRandomSize()) { mockk() }
        val returnList = mutableListOf<NoteItem>()
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.isListHide() } returns isListHide

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.notifyList(any())
            callback.onBindingList()
            interactor.getCount()
            interactor.isListHide()
            callback.notifyList(returnList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }

    @Test fun onShowOptionsDialog() {
        TODO()
        //        viewModel.onShowOptionsDialog(, Random.nextInt())
        //
        //        val itemArray0 = Array(getRandomSize()) { nextString() }
        //        val itemArray1 = Array(getRandomSize()) { nextString() }
        //        val itemArray2 = Array(getRandomSize()) { nextString() }
        //        val itemArray3 = Array(getRandomSize()) { nextString() }
        //
        //        val textItem = mockk<NoteItem.Text>()
        //        val rollItem = mockk<NoteItem.Roll>()
        //        val untitledName = nextString()
        //        val name = nextString()
        //
        //        val updateString = nextString()
        //        val setString = nextString()
        //        val unbindString = nextString()
        //        val bindString = nextString()
        //
        //        viewModel.itemList.clear()
        //        viewModel.itemList.addAll(listOf(textItem, rollItem))
        //
        //        every { callback.getString(R.string.hint_text_name) } returns untitledName
        //
        //        every { callback.getString(R.string.dialog_menu_notification_update) } returns updateString
        //        every { callback.getString(R.string.dialog_menu_notification_set) } returns setString
        //        every { callback.getString(R.string.dialog_menu_status_unbind) } returns unbindString
        //        every { callback.getString(R.string.dialog_menu_status_bind) } returns bindString
        //
        //        every { textItem.name } returns ""
        //        every { callback.getStringArray(R.array.dialog_menu_text) } returns itemArray0.copyOf()
        //        every { textItem.haveAlarm() } returns false
        //        every { textItem.isStatus } returns true
        //        viewModel.onShowOptionsDialog(, p = 0)
        //
        //        every { textItem.name } returns name
        //        every { callback.getStringArray(R.array.dialog_menu_text) } returns itemArray1.copyOf()
        //        every { textItem.haveAlarm() } returns true
        //        every { textItem.isStatus } returns false
        //        viewModel.onShowOptionsDialog(, p = 0)
        //
        //        every { rollItem.name } returns ""
        //        every { callback.getStringArray(R.array.dialog_menu_roll) } returns itemArray2.copyOf()
        //        every { rollItem.haveAlarm() } returns false
        //        every { rollItem.isStatus } returns false
        //        viewModel.onShowOptionsDialog(, p = 1)
        //
        //        every { rollItem.name } returns name
        //        every { callback.getStringArray(R.array.dialog_menu_roll) } returns itemArray3.copyOf()
        //        every { rollItem.haveAlarm() } returns true
        //        every { rollItem.isStatus } returns true
        //        viewModel.onShowOptionsDialog(, p = 1)
        //
        //        verifySequence {
        //            textItem.name
        //            callback.getString(R.string.hint_text_name)
        //            callback.getStringArray(R.array.dialog_menu_text)
        //            textItem.haveAlarm()
        //            callback.getString(R.string.dialog_menu_notification_set)
        //            textItem.isStatus
        //            callback.getString(R.string.dialog_menu_status_unbind)
        //            callback.showOptionsDialog(untitledName, itemArray0.copyOf().apply {
        //                set(Options.Notes.NOTIFICATION, setString)
        //                set(Options.Notes.BIND, unbindString)
        //            }, p = 0)
        //
        //            textItem.name
        //            callback.getStringArray(R.array.dialog_menu_text)
        //            textItem.haveAlarm()
        //            callback.getString(R.string.dialog_menu_notification_update)
        //            textItem.isStatus
        //            callback.getString(R.string.dialog_menu_status_bind)
        //            callback.showOptionsDialog(name, itemArray1.copyOf().apply {
        //                set(Options.Notes.NOTIFICATION, updateString)
        //                set(Options.Notes.BIND, bindString)
        //            }, p = 0)
        //
        //            rollItem.name
        //            callback.getString(R.string.hint_text_name)
        //            callback.getStringArray(R.array.dialog_menu_roll)
        //            rollItem.haveAlarm()
        //            callback.getString(R.string.dialog_menu_notification_set)
        //            rollItem.isStatus
        //            callback.getString(R.string.dialog_menu_status_bind)
        //            callback.showOptionsDialog(untitledName, itemArray2.copyOf().apply {
        //                set(Options.Notes.NOTIFICATION, setString)
        //                set(Options.Notes.BIND, bindString)
        //            }, p = 1)
        //
        //            rollItem.name
        //            callback.getStringArray(R.array.dialog_menu_roll)
        //            rollItem.haveAlarm()
        //            callback.getString(R.string.dialog_menu_notification_update)
        //            rollItem.isStatus
        //            callback.getString(R.string.dialog_menu_status_unbind)
        //            callback.showOptionsDialog(name, itemArray3.copyOf().apply {
        //                set(Options.Notes.NOTIFICATION, updateString)
        //                set(Options.Notes.BIND, unbindString)
        //            }, p = 1)
        //        }
    }

    @Test fun onResultOptionsDialog() {
        val p = Random.nextInt()
        val which = -1

        spyViewModel.onResultOptionsDialog(p, which)

        every { spyViewModel.onMenuNotification(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Notes.NOTIFICATION)

        every { spyViewModel.onMenuBind(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Notes.BIND)

        every { spyViewModel.onMenuConvert(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Notes.CONVERT)

        every { spyViewModel.onMenuCopy(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Notes.COPY)

        every { spyViewModel.onMenuDelete(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Notes.DELETE)

        verifySequence {
            spyViewModel.onResultOptionsDialog(p, which)

            spyViewModel.onResultOptionsDialog(p, Options.Notes.NOTIFICATION)
            spyViewModel.onMenuNotification(p)

            spyViewModel.onResultOptionsDialog(p, Options.Notes.BIND)
            spyViewModel.onMenuBind(p)

            spyViewModel.onResultOptionsDialog(p, Options.Notes.CONVERT)
            spyViewModel.onMenuConvert(p)

            spyViewModel.onResultOptionsDialog(p, Options.Notes.COPY)
            spyViewModel.onMenuCopy(p)

            spyViewModel.onResultOptionsDialog(p, Options.Notes.DELETE)
            spyViewModel.onMenuDelete(p)
        }
    }

    @Test fun onMenuNotification() {
        viewModel.onMenuNotification(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]
        val alarmDate = nextString()
        val calendar = mockk<Calendar>()
        val haveAlarm = Random.nextBoolean()

        every { item.alarmDate } returns alarmDate
        FastMock.timeExtension()
        every { alarmDate.toCalendar() } returns calendar
        every { item.haveAlarm() } returns haveAlarm

        viewModel.itemList.clearAdd(itemList)
        viewModel.onMenuNotification(index)

        verifySequence {
            item.alarmDate
            alarmDate.toCalendar()
            item.haveAlarm()
            callback.showDateDialog(calendar, haveAlarm, index)
        }

        assertEquals(itemList, viewModel.itemList)
    }

    @Test fun onMenuBind() = startCoTest {
        viewModel.onMenuBind(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        every { item.switchStatus() } returns item

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuBind(index)

        coVerifySequence {
            callback.notifyList(itemList)

            updateNote(item)
            callback.sendNotifyNotesBroadcast()

        }
        assertEquals(itemList, viewModel.itemList)
    }

    @Test fun onMenuConvert() = startCoTest {
        viewModel.onMenuConvert(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]
        val convertItem = mockk<NoteItem>()
        val resultList = List<NoteItem>(getRandomSize()) { mockk() }
        val sort = mockk<Sort>()

        coEvery { interactor.convertNote(item) } returns convertItem
        every { preferencesRepo.sort } returns sort
        coEvery { sortList(any(), sort) } returns resultList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuConvert(index)

        coVerifySequence {
            interactor.convertNote(item)
            preferencesRepo.sort
            sortList(any(), sort)

            callback.notifyList(resultList)
            callback.sendNotifyNotesBroadcast()
        }

        assertEquals(resultList, viewModel.itemList)
    }

    @Test fun onMenuCopy() {
        viewModel.onMenuCopy(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]
        val text = nextString()

        coEvery { getCopyText(item) } returns text

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuCopy(index)

        coVerifySequence {
            getCopyText(item)
            callback.copyClipboard(text)
        }

        assertEquals(itemList, viewModel.itemList)
    }

    @Test fun onMenuDelete() {
        viewModel.onMenuDelete(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        val resultList = ArrayList(itemList)
        resultList.removeAt(index)

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuDelete(index)

        coVerifySequence {
            callback.notifyList(resultList)

            deleteNote(item)

            callback.sendCancelAlarmBroadcast(item)
            callback.sendCancelNoteBroadcast(item)
            callback.sendNotifyInfoBroadcast()
        }

        assertEquals(resultList, viewModel.itemList)
    }


    @Test fun onResultDateDialog() = startCoTest {
        val p = Random.nextInt()
        val dateList = mockk<List<String>>()

        coEvery { getNotificationDateList() } returns dateList
        viewModel.onResultDateDialog(calendar, p)

        coVerifySequence {
            getNotificationDateList()
            callback.showTimeDialog(calendar, dateList, p)
        }
    }

    @Test fun onResultDateDialogClear() = startCoTest {
        viewModel.onResultDateDialogClear(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        viewModel.itemList.clearAdd(itemList)

        every { item.clearAlarm() } returns item

        viewModel.onResultDateDialogClear(index)

        coVerifySequence {
            item.clearAlarm()
            callback.notifyList(itemList)

            deleteNotification(item)

            callback.sendCancelAlarmBroadcast(item)
            callback.sendNotifyInfoBroadcast()
        }

        assertEquals(itemList, viewModel.itemList)
    }

    @Test fun onResultTimeDialog() = startCoTest {
        val calendar = mockk<Calendar>()
        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]
        val id = Random.nextLong()

        FastMock.timeExtension()

        every { calendar.isBeforeNow() } returns true
        viewModel.onResultTimeDialog(calendar, Random.nextInt())

        every { calendar.isBeforeNow() } returns false
        viewModel.onResultTimeDialog(calendar, Random.nextInt())

        every { item.id } returns id
        viewModel.itemList.clearAdd(itemList)
        viewModel.onResultTimeDialog(calendar, index)

        coVerifySequence {
            calendar.isBeforeNow()

            calendar.isBeforeNow()

            calendar.isBeforeNow()
            setNotification(item, calendar)
            callback.notifyList(itemList)

            item.id
            callback.sendSetAlarmBroadcast(id, calendar)
            callback.sendNotifyInfoBroadcast()
        }

        assertEquals(itemList, viewModel.itemList)
    }


    @Test fun onReceiveUnbindNote() {
        viewModel.onReceiveUnbindNote(Random.nextLong())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onReceiveUnbindNote(item.id)
        item.isStatus = false

        verifySequence { callback.notifyList(itemList) }
    }

    @Test fun onReceiveUpdateAlarm() = startCoTest {
        TODO()
        //        viewModel.onReceiveUpdateAlarm(Random.nextLong())
        //
        //        val itemList = data.itemList
        //        viewModel.itemList.clearAdd(itemList)
        //        assertEquals(itemList, viewModel.itemList)
        //
        //        val notificationItem = TestData.Notification.firstNotification
        //        val p = itemList.indices.random()
        //        val item = itemList[p]
        //
        //        coEvery { getNotification(item.id) } returns null
        //
        //        viewModel.onReceiveUpdateAlarm(item.id)
        //
        //        coEvery { getNotification(item.id) } returns notificationItem
        //
        //        viewModel.onReceiveUpdateAlarm(item.id)
        //        item.apply {
        //            alarmId = notificationItem.alarm.id
        //            alarmDate = notificationItem.alarm.date
        //        }
        //
        //        coVerifySequence {
        //            getNotification(item.id)
        //
        //            getNotification(item.id)
        //            callback.notifyList(itemList)
        //        }
    }
}