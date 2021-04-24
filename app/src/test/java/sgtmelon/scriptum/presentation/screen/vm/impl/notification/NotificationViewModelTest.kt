package sgtmelon.scriptum.presentation.screen.vm.impl.notification

import android.os.Bundle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.model.data.IntentData.Snackbar
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.isDivideTwoEntirely
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
    private val spyViewModel by lazy { spyk(viewModel) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)

        assertTrue(viewModel.cancelList.isEmpty())
        assertTrue(viewModel.itemList.isEmpty())
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        val bundle = mockk<Bundle>()

        viewModel.onSetup()

        every { spyViewModel.restoreSnackbar(bundle) } returns Unit
        spyViewModel.onSetup(bundle)

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
            callback.setupInsets()

            spyViewModel.onSetup(bundle)
            spyViewModel.callback
            callback.setupToolbar()
            spyViewModel.callback
            callback.setupRecycler()
            spyViewModel.callback
            callback.setupInsets()
            spyViewModel.restoreSnackbar(bundle)
        }
    }

    @Test fun restoreSnackbar() {
        val bundle = mockk<Bundle>()

        val size = getRandomSize()
        val positionArray = IntArray(size) { Random.nextInt() }
        val jsonArray = Array(size) { nextString() }
        val itemList = List<NotificationItem?>(size) {
            if (it.isDivideTwoEntirely()) mockk() else null
        }

        val cancelList = mutableListOf<Pair<Int, NotificationItem>>()

        mockkObject(NotificationItem)
        for ((i, item) in itemList.withIndex()) {
            every { NotificationItem[jsonArray[i]] } returns item

            if (item != null) {
                cancelList.add(Pair(positionArray[i], item))
            }
        }

        every { bundle.getIntArray(Snackbar.Intent.POSITIONS) } returns null
        viewModel.restoreSnackbar(bundle)

        assertTrue(viewModel.cancelList.isEmpty())

        every { bundle.getIntArray(Snackbar.Intent.POSITIONS) } returns positionArray
        every { bundle.getStringArray(Snackbar.Intent.ITEMS) } returns null
        viewModel.restoreSnackbar(bundle)

        assertTrue(viewModel.cancelList.isEmpty())

        every { bundle.getStringArray(Snackbar.Intent.ITEMS) } returns jsonArray
        viewModel.restoreSnackbar(bundle)

        assertEquals(cancelList, viewModel.cancelList)

        verifySequence {
            bundle.getIntArray(Snackbar.Intent.POSITIONS)

            bundle.getIntArray(Snackbar.Intent.POSITIONS)
            bundle.getStringArray(Snackbar.Intent.ITEMS)

            bundle.getIntArray(Snackbar.Intent.POSITIONS)
            bundle.getStringArray(Snackbar.Intent.ITEMS)
            for (i in itemList.indices) {
                NotificationItem[jsonArray[i]]
            }
            callback.showSnackbar()
        }
    }

    @Test fun onSaveData() {
        val size = getRandomSize()
        val positionArray = IntArray(size) { Random.nextInt() }
        val jsonArray = Array(size) { nextString() }
        val itemList = List(size) { mockk<NotificationItem>() }

        val bundle = mockk<Bundle>()

        for ((i, item) in itemList.withIndex()) {
            every { item.toJson() } returns jsonArray[i]

            viewModel.cancelList.add(Pair(positionArray[i], item))
        }

        every { bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray) } returns Unit
        every { bundle.putStringArray(Snackbar.Intent.ITEMS, jsonArray) } returns Unit

        viewModel.onSaveData(bundle)
        assertTrue(viewModel.cancelList.isEmpty())

        verifySequence {
            bundle.putIntArray(Snackbar.Intent.POSITIONS, positionArray)
            bundle.putStringArray(Snackbar.Intent.ITEMS, jsonArray)
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