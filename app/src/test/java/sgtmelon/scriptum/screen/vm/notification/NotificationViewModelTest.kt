package sgtmelon.scriptum.screen.vm.notification

import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
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
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class NotificationViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: INotificationActivity

    @MockK lateinit var interactor: INotificationInteractor

    private val viewModel by lazy { NotificationViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        assertEquals(callback, viewModel.callback)

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

        verify(exactly = 1) {
            callback.setupToolbar()
            callback.setupRecycler(interactor.theme)
        }
    }

    @Test fun onUpdateData() {

    }

    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        viewModel.itemList.addAll(itemList)
        val index = itemList.indices.random()

        viewModel.onClickNote(index)
        verify(exactly = 1) { callback.startNoteActivity(itemList[index]) }
    }

    @Test fun onClickCancel() {
        viewModel.onClickCancel(Random.nextInt())

        val itemList = itemList.toMutableList()
        viewModel.itemList.addAll(itemList)

        val index = itemList.indices.random()
        viewModel.onClickCancel(index)

        val item = itemList.removeAt(index)

        coVerify { interactor.cancelNotification(item) }
        verify(exactly = 1) {
            callback.notifyInfoBind(itemList.size)
            callback.notifyItemRemoved(itemList, index)
        }
    }


    private val itemList = listOf(
            NotificationItem(
                    Note(id = 0, name = "testName1", color = 5, type = NoteType.TEXT),
                    Alarm(id = 0, date = "123")
            ),
            NotificationItem(
                    Note(id = 1, name = "testName2", color = 3, type = NoteType.ROLL),
                    Alarm(id = 1, date = "456")
            ),
            NotificationItem(
                    Note(id = 2, name = "testName3", color = 8, type = NoteType.TEXT),
                    Alarm(id = 2, date = "789")
            )
    )

}