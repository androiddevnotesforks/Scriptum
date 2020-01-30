package sgtmelon.scriptum.screen.vm.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.model.item.NotificationItem.Alarm
import sgtmelon.scriptum.model.item.NotificationItem.Note
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.notification.INotificationActivity
import kotlin.random.Random

/**
 * Test for [NotificationViewModel].
 */
@ExperimentalCoroutinesApi
class NotificationViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: INotificationActivity

    @MockK lateinit var interactor: INotificationInteractor

    private val viewModel by lazy { NotificationViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }


    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verify(exactly = 1) { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        every { interactor.theme } returns Theme.LIGHT

        viewModel.onSetup()

        verify(ordering = Ordering.ALL) {
            callback.setupToolbar()
            callback.setupRecycler(interactor.theme)
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns itemList.toMutableList()

        viewModel.onUpdateData()

        coVerify(ordering = Ordering.SEQUENCE) {
            callback.beforeLoad()

            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.onUpdateData()

        coVerify(ordering = Ordering.SEQUENCE) {
            callback.beforeLoad()

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns itemList.toMutableList()

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onUpdateData()


        coVerify(ordering = Ordering.SEQUENCE) {
            callback.beforeLoad()
            updateList(itemList)

            interactor.getCount()
            interactor.getList()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerify(ordering = Ordering.SEQUENCE) {
            callback.beforeLoad()
            updateList(mutableListOf())
            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    private fun updateList(itemList: List<NotificationItem>) {
        callback.notifyList(itemList)
        callback.onBindingList()
    }


    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val index = itemList.indices.random()

        viewModel.onClickNote(index)
        verify(exactly = 1) { callback.startNoteActivity(itemList[index]) }
    }

    @Test fun onClickCancel() = startCoTest {
        viewModel.onClickCancel(Random.nextInt())

        val itemList = itemList.toMutableList()

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val index = itemList.indices.random()
        val item = itemList.removeAt(index)

        viewModel.onClickCancel(index)

        coVerify(ordering = Ordering.ALL) {
            interactor.cancelNotification(item)

            callback.notifyInfoBind(itemList.size)
            callback.notifyItemRemoved(itemList, index)
        }
    }


    private val itemFirst = NotificationItem(
            Note(id = 0, name = "testName1", color = 5, type = NoteType.TEXT),
            Alarm(id = 0, date = "123")
    )

    private val itemSecond = NotificationItem(
            Note(id = 1, name = "testName2", color = 3, type = NoteType.ROLL),
            Alarm(id = 1, date = "456")
    )

    private val itemThird = NotificationItem(
            Note(id = 2, name = "testName3", color = 8, type = NoteType.TEXT),
            Alarm(id = 2, date = "789")
    )

    private val itemList = listOf(itemFirst, itemSecond, itemThird)

}