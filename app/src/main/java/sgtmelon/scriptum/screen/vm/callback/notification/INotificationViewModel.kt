package sgtmelon.scriptum.screen.vm.callback.notification

import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.screen.vm.notification.NotificationViewModel

/**
 * Интерфейс для общения [NotificationActivity] с [NotificationViewModel]
 *
 * @author SerjantArbuz
 */
interface INotificationViewModel : IParentViewModel {

    fun onSetup()

    fun onUpdateData()

    fun onClickNote(p: Int)

    fun onClickCancel(p: Int)

}