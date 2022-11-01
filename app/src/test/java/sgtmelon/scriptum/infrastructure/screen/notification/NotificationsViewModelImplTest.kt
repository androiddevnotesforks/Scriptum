package sgtmelon.scriptum.infrastructure.screen.notification

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.GetNotificationListUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsViewModelImpl
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [NotificationsViewModelImpl].
 */
class NotificationsViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    private val data = TestData.Notification

    @MockK lateinit var setNotification: SetNotificationUseCase
    @MockK lateinit var deleteNotification: DeleteNotificationUseCase
    @MockK lateinit var getList: GetNotificationListUseCase

    private val viewModel by lazy {
        NotificationsViewModelImpl(setNotification, deleteNotification, getList)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(setNotification, deleteNotification, getList)
    }

    //endregion

    @Test fun getShowList() {
        TODO()
    }

    @Test fun getUpdateList() {
        TODO()
    }

    @Test fun getItemList() {
        TODO()
    }

    @Test fun getShowSnackbar() {
        TODO()
    }

    @Test fun removeNotification() {
        TODO()
    }

    @Test fun undoRemove() {
        TODO()
    }

    @Test fun clearUndoStack() {
        TODO()
    }
}