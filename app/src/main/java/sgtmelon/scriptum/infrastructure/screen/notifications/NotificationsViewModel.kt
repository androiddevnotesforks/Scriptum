package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.screen.notifications.state.UndoState
import sgtmelon.scriptum.infrastructure.screen.parent.list.CustomListNotifyViewModelFacade

interface NotificationsViewModel : CustomListNotifyViewModelFacade<NotificationItem> {

    val showSnackbar: LiveData<Boolean>

    fun removeNotification(p: Int): Flow<Pair<NotificationItem, Int>>

    fun undoRemove(): Flow<UndoState>

    fun clearUndoStack()

}