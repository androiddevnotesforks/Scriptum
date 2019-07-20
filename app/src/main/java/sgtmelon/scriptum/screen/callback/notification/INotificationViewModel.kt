package sgtmelon.scriptum.screen.callback.notification

import sgtmelon.scriptum.screen.callback.IParentViewModel
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
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