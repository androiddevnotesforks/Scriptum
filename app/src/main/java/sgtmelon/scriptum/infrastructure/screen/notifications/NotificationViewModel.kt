package sgtmelon.scriptum.infrastructure.screen.notifications

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.notification.INotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel

/**
 * Interface for communication [INotificationActivity] with [NotificationViewModelImpl].
 */
interface NotificationViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()

    fun onClickCancel(p: Int)


    fun onSnackbarAction()

    fun onSnackbarDismiss()

}