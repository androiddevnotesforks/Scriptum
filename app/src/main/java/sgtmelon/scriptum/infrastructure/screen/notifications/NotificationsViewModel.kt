package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.receiver.screen.InfoChangeReceiver
import sgtmelon.scriptum.infrastructure.screen.notifications.state.UndoState
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListViewModel

interface NotificationsViewModel : ListViewModel<NotificationItem>,
    InfoChangeReceiver.Callback {

    val showSnackbar: LiveData<Boolean>

    fun updateData()

    fun removeItem(position: Int): Flow<Pair<NotificationItem, Int>>

    fun undoRemove(): Flow<UndoState>

    fun clearUndoStack()

    fun getNote(item: NotificationItem): Flow<NoteItem>

}