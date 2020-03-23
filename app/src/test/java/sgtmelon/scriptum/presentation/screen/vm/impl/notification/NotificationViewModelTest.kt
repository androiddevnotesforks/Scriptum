package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NotificationItem
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
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        every { interactor.theme } returns Theme.LIGHT

        viewModel.onSetup()

        verifyAll {
            callback.setupToolbar()
            callback.setupRecycler(interactor.theme)
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns data.itemList.size
        coEvery { interactor.getList() } returns data.itemList

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()

            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            updateList(data.itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val returnList = mutableListOf(data.itemList.first())

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

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
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    private fun updateList(itemList: List<NotificationItem>) = with(callback) {
        notifyList(itemList)
        onBindingList()
    }


    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        val p = data.itemList.indices.random()

        viewModel.onClickNote(p)
        verifySequence { callback.startNoteActivity(data.itemList[p]) }
    }

    @Test fun onClickCancel() = startCoTest {
        viewModel.onClickCancel(Random.nextInt())

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList.removeAt(p)

        viewModel.onClickCancel(p)

        coVerifySequence {
            interactor.cancelNotification(item)

            callback.notifyInfoBind(itemList.size)
            callback.notifyItemRemoved(itemList, p)
        }
    }

}