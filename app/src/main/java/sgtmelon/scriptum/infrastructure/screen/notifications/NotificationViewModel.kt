package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.model.state.ShowListState
import sgtmelon.scriptum.infrastructure.system.delegators.SnackbarDelegator

interface NotificationViewModel : SnackbarDelegator.Callback {

    val showList: LiveData<ShowListState>

    val updateList: UpdateListState

    val itemList: LiveData<List<NotificationItem>>

    val showSnackbar: LiveData<Boolean>

    fun notifyListState()

    fun removeNotification(p: Int): Flow<Pair<NotificationItem, Int>>

}