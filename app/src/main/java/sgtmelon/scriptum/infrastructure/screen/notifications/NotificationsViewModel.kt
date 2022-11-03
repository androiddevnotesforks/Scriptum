package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.model.state.ShowListState

interface NotificationsViewModel {

    val showList: LiveData<ShowListState>

    val itemList: LiveData<List<NotificationItem>>

    val updateList: UpdateListState

    val showSnackbar: LiveData<Boolean>

    fun removeNotification(p: Int): Flow<Pair<NotificationItem, Int>>

    fun undoRemove(): Flow<UndoState>

    fun clearUndoStack()

}