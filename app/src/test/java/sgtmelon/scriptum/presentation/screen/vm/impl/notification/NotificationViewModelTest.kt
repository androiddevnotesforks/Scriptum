package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.screen.ui.callback.notification.INotificationActivity
import kotlin.random.Random

/**
 * Test for [NotificationViewModel].
 */
@ExperimentalCoroutinesApi
class NotificationViewModelTest : ParentViewModelTest() {

    private val data = TestData.Notification

    @MockK lateinit var callback: INotificationActivity

    @MockK lateinit var interactor: INotificationInteractor

    private val viewModel by lazy { NotificationViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)

        assertTrue(viewModel.cancelList.isEmpty())
        assertTrue(viewModel.itemList.isEmpty())
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
            callback.setupInsets()
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = data.itemList

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns itemList

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<NotificationItem>()

        coEvery { interactor.getCount() } returns itemList.size
        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            interactor.getCount()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = data.itemList.apply { shuffle() }

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())
            interactor.getCount()
            interactor.getList()
            updateList(returnList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns returnList.size

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())
            interactor.getCount()
            updateList(any())
        }
    }

    private fun updateList(itemList: List<NotificationItem>) = with(callback) {
        notifyList(itemList)
        onBindingList()
    }


    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()

        viewModel.onClickNote(p)
        verifySequence { callback.openNoteScreen(itemList[p]) }
    }

    @Test fun onClickCancel() = startCoTest {
        viewModel.onClickCancel(Random.nextInt())

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)

        assertEquals(itemList, viewModel.itemList)
        assertTrue(viewModel.cancelList.isEmpty())

        val p = itemList.indices.random()
        val item = itemList.removeAt(p)

        viewModel.onClickCancel(p)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(mutableListOf(Pair(p, item)), viewModel.cancelList)

        coVerifySequence {
            interactor.cancelNotification(item)

            callback.apply {
                notifyInfoBind(itemList.size)
                notifyItemRemoved(itemList, p)
                showSnackbar()
            }
        }
    }


    @Test fun onSnackbarAction_correct() {
        viewModel.onSnackbarAction()

        val item = mockk<NotificationItem>()
        val newItem = mockk<NotificationItem>()

        val firstPair = Pair(Random.nextInt(), mockk<NotificationItem>())
        val secondPair = Pair(0, item)

        val itemList = mutableListOf(mockk<NotificationItem>())
        val cancelList = mutableListOf(firstPair, secondPair)

        viewModel.itemList.clearAdd(itemList)
        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coEvery { interactor.setNotification(item) } returns newItem
        viewModel.onSnackbarAction()

        itemList.add(0, newItem)
        cancelList.removeAt(index = 1)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            callback.apply {
                notifyInfoBind(itemList.size)
                notifyItemInsertedScroll(itemList, secondPair.first)
                showSnackbar()
            }

            interactor.setNotification(item)
            callback.setList(itemList)
        }
    }

    @Test fun onSnackbarAction_incorrect() {
        viewModel.onSnackbarAction()

        val item = mockk<NotificationItem>()
        val newItem = mockk<NotificationItem>()

        val pair = Pair(Random.nextInt(), item)

        val itemList = mutableListOf<NotificationItem>()
        val cancelList = mutableListOf(pair)

        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coEvery { interactor.setNotification(item) } returns newItem
        viewModel.onSnackbarAction()

        itemList.add(0, newItem)
        cancelList.removeAt(index = 0)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            callback.apply {
                notifyInfoBind(itemList.size)
                notifyItemInsertedScroll(itemList, itemList.lastIndex)
                onBindingList()
            }

            interactor.setNotification(item)
            callback.setList(itemList)
        }
    }

    @Test fun onSnackbarDismiss() {
        val size = getRandomSize()
        val cancelList = MutableList(size) { Pair(Random.nextInt(), mockk<NotificationItem>()) }

        viewModel.cancelList.clearAdd(cancelList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarDismiss()

        assertTrue(viewModel.cancelList.isEmpty())
    }

}