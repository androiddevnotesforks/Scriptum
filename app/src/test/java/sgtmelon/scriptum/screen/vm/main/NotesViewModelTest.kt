package sgtmelon.scriptum.screen.vm.main

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
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.model.annotation.Options
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.main.INotesFragment
import sgtmelon.scriptum.screen.vm.main.NotesViewModel.Companion.sort
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
        val theme = Theme.DARK

        every { interactor.theme } returns theme

        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            interactor.theme
            callback.setupRecycler(theme)
            callback.setupDialog()
        }
    }

    @Test fun onUpdateData() {
        TODO()
    }

    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
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
        itemList[1].type = NoteType.ROLL

        viewModel.itemList.addAll(itemList)
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

        viewModel.itemList.addAll(itemList)
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

        viewModel.itemList.addAll(itemList)
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

        every { interactor.sort } returns Sort.CREATE

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = data.itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Notes.CONVERT)
        val sortList = itemList.sort(Sort.CREATE)

        coVerifySequence {
            interactor.convert(item)
            interactor.sort

            callback.notifyList(sortList)
        }
    }

    @Test fun onResultOptionsDialog_onCopy() {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.COPY)

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = data.itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Notes.COPY)

        coVerifySequence { interactor.copy(item) }
    }

    @Test fun onResultOptionsDialog_onDelete() {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Notes.DELETE)

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
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

        coEvery { interactor.getDateList() } returns dateList

        viewModel.onResultDateDialog(calendar, p)

        coVerifySequence {
            interactor.getDateList()
            callback.showTimeDialog(calendar, dateList, p)
        }
    }

    @Test fun onResultDateDialogClear() = startCoTest {
        viewModel.onResultDateDialogClear(Random.nextInt())

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
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

        viewModel.itemList.addAll(itemList)
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

        viewModel.itemList.addAll(itemList)
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

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val notificationItem = TestData.Notification.notificationFirst
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