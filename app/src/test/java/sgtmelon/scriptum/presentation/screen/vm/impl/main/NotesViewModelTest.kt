package sgtmelon.scriptum.presentation.screen.vm.impl.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getCalendar
import sgtmelon.extension.getRandomFutureTime
import sgtmelon.extension.getRandomPastTime
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.model.annotation.Options
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.sort
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesFragment
import java.util.*
import kotlin.random.Random

/**
 * Test for [NotesViewModel].
 */
@ExperimentalCoroutinesApi
class NotesViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: INotesFragment

    @MockK lateinit var interactor: INotesInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val calendar = mockkClass(Calendar::class)

    private val viewModel by lazy { NotesViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        val themeList = listOf(null, Theme.LIGHT, Random.nextInt(), null)

        themeList.forEach {
            every { interactor.theme } returns it
            viewModel.onSetup()
        }

        verifySequence {
            themeList.forEach {
                interactor.theme

                if (it != null) {
                    callback.setupToolbar()
                    callback.setupRecycler(it)
                    callback.setupDialog()
                }
            }
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = data.itemList
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns null
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns null
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coEvery { interactor.getList() } returns itemList
        coEvery { interactor.isListHide() } returns null
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coEvery { interactor.isListHide() } returns isListHide
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            interactor.getCount()

            callback.beforeLoad()
            interactor.getCount()
            callback.showProgress()
            interactor.getList()

            callback.beforeLoad()
            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            interactor.isListHide()

            callback.beforeLoad()
            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            interactor.isListHide()
            callback.notifyList(itemList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<NoteItem>()
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns null
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.isListHide() } returns null
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coEvery { interactor.isListHide() } returns isListHide
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            interactor.getCount()

            callback.beforeLoad()
            interactor.getCount()
            interactor.isListHide()

            callback.beforeLoad()
            interactor.getCount()
            interactor.isListHide()
            callback.notifyList(itemList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = data.itemList.apply { shuffle() }
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns null
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns null
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coEvery { interactor.getList() } returns returnList
        coEvery { interactor.isListHide() } returns null
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coEvery { interactor.isListHide() } returns isListHide
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            callback.notifyList(returnList)
            callback.onBindingList()
            interactor.getCount()

            callback.beforeLoad()
            callback.notifyList(returnList)
            callback.onBindingList()
            interactor.getCount()
            interactor.getList()

            callback.beforeLoad()
            callback.notifyList(returnList)
            callback.onBindingList()
            interactor.getCount()
            interactor.getList()
            interactor.isListHide()

            callback.beforeLoad()
            callback.notifyList(returnList)
            callback.onBindingList()
            interactor.getCount()
            interactor.getList()
            interactor.isListHide()
            callback.notifyList(returnList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = mutableListOf<NoteItem>()
        val isListHide = Random.nextBoolean()

        coEvery { interactor.getCount() } returns null
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.isListHide() } returns null
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coEvery { interactor.isListHide() } returns isListHide
        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            callback.notifyList(any())
            callback.onBindingList()
            interactor.getCount()

            callback.beforeLoad()
            callback.notifyList(any())
            callback.onBindingList()
            interactor.getCount()
            interactor.isListHide()

            callback.beforeLoad()
            callback.notifyList(any())
            callback.onBindingList()
            interactor.getCount()
            interactor.isListHide()
            callback.notifyList(returnList)
            callback.setupBinding(isListHide)
            callback.onBindingList()
        }
    }


    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onClickNote(p)

        verifySequence { callback.startNoteActivity(item) }
    }

    @Test fun onShowOptionsDialog() {
        viewModel.onShowOptionsDialog(Random.nextInt())

        val textArray = arrayOf("", "", "text")
        val rollArray = arrayOf("", "", "roll")

        every { callback.getStringArray(R.array.dialog_menu_text) } returns textArray
        every { callback.getStringArray(R.array.dialog_menu_roll) } returns rollArray

        every { callback.getString(R.string.dialog_menu_notification_update) } returns "update"
        every { callback.getString(R.string.dialog_menu_notification_set) } returns "set"

        every { callback.getString(R.string.dialog_menu_status_unbind) } returns "unbind"
        every { callback.getString(R.string.dialog_menu_status_bind) } returns "bind"

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onShowOptionsDialog(p = 0)
        viewModel.onShowOptionsDialog(p = 1)

        verifySequence {
            callback.getStringArray(R.array.dialog_menu_text)
            callback.getString(R.string.dialog_menu_notification_update)
            callback.getString(R.string.dialog_menu_status_bind)

            callback.showOptionsDialog(textArray.apply {
                set(0, "update")
                set(1, "bind")
            }, p = 0)

            callback.getStringArray(R.array.dialog_menu_roll)
            callback.getString(R.string.dialog_menu_notification_set)
            callback.getString(R.string.dialog_menu_status_unbind)

            callback.showOptionsDialog(rollArray.apply {
                set(0, "set")
                set(1, "unbind")
            }, p = 1)
        }
    }

    @Test fun onResultOptionsDialog_onNotification() {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.NOTIFICATION)

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = 0
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Notes.NOTIFICATION)

        verifySequence {
            callback.showDateDialog(item.alarmDate.getCalendar(), item.haveAlarm(), p)
        }
    }

    @Test fun onResultOptionsDialog_onBind() = startCoTest {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.BIND)

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Notes.BIND)
        item.switchStatus()

        coVerifySequence {
            callback.notifyItemChanged(itemList, p)

            interactor.updateNote(item)
        }
    }

    @Test fun onResultOptionsDialog_onConvert() = startCoTest {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.CONVERT)

        val sort = TestData.sort

        every { interactor.sort } returns sort

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val item = itemList[0]
        val itemReturn = itemList[1]

        coEvery { interactor.convertNote(item) } returns null
        viewModel.onResultOptionsDialog(0, Options.Notes.CONVERT)

        coEvery { interactor.convertNote(item) } returns itemReturn
        viewModel.onResultOptionsDialog(0, Options.Notes.CONVERT)

        itemList[0] = itemReturn
        val sortList = itemList.sort(sort)

        coVerifySequence {
            interactor.convertNote(item)

            interactor.convertNote(item)
            interactor.sort
            callback.notifyList(sortList)
        }
    }

    @Test fun onResultOptionsDialog_onCopy() {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.COPY)

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = data.itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Notes.COPY)

        coVerifySequence { interactor.copy(item) }
    }

    @Test fun onResultOptionsDialog_onDelete() {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.DELETE)

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList.removeAt(p)

        viewModel.onResultOptionsDialog(p, Options.Notes.DELETE)

        coVerifySequence {
            callback.notifyItemRemoved(itemList, p)

            interactor.deleteNote(item)
            bindInteractor.notifyInfoBind(callback)
        }
    }


    @Test fun onResultDateDialog() = startCoTest {
        val p = Random.nextInt()
        val dateList = data.dateList

        coEvery { interactor.getDateList() } returns null
        viewModel.onResultDateDialog(calendar, p)

        coEvery { interactor.getDateList() } returns dateList
        viewModel.onResultDateDialog(calendar, p)

        coVerifySequence {
            interactor.getDateList()

            interactor.getDateList()
            callback.showTimeDialog(calendar, dateList, p)
        }
    }

    @Test fun onResultDateDialogClear() = startCoTest {
        viewModel.onResultDateDialogClear(Random.nextInt())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultDateDialogClear(p)
        item.clearAlarm()

        coVerifySequence {
            callback.notifyItemChanged(itemList, p)

            interactor.clearDate(item)
            bindInteractor.notifyInfoBind(callback)
        }
    }

    @Test fun onResultTimeDialog() = startCoTest {
        val calendarPast = getRandomPastTime().getCalendar()
        viewModel.onResultTimeDialog(calendarPast, Random.nextInt())

        val calendarFuture = getRandomFutureTime().getCalendar()
        viewModel.onResultTimeDialog(calendarFuture, Random.nextInt())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultTimeDialog(calendarFuture, p)

        coVerifySequence {
            interactor.setDate(item, calendarFuture)
            callback.notifyItemChanged(itemList, p)

            bindInteractor.notifyInfoBind(callback)
        }
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

        verifySequence { callback.notifyItemChanged(itemList, p) }
    }

    @Test fun onReceiveUpdateAlarm() = startCoTest {
        viewModel.onReceiveUpdateAlarm(Random.nextLong())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val notificationItem = TestData.Notification.firstNotification
        val p = itemList.indices.random()
        val item = itemList[p]

        coEvery { interactor.getNotification(item.id) } returns null

        viewModel.onReceiveUpdateAlarm(item.id)

        coEvery { interactor.getNotification(item.id) } returns notificationItem

        viewModel.onReceiveUpdateAlarm(item.id)
        item.apply {
            alarmId = notificationItem.alarm.id
            alarmDate = notificationItem.alarm.date
        }

        coVerifySequence {
            interactor.getNotification(item.id)

            interactor.getNotification(item.id)
            callback.notifyItemChanged(itemList, p)
        }
    }


    @Test fun sort() = with(data) {
        assertEquals(changeList, itemList.sort(Sort.CHANGE))
        assertEquals(createList, itemList.sort(Sort.CREATE))
        assertEquals(rankList, itemList.sort(Sort.RANK))
        assertEquals(colorList, itemList.sort(Sort.COLOR))
    }

}