package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationViewModel

/**
 * Interactor for [NotificationViewModel].
 */
@Deprecated("Don't use it")
class NotificationInteractor(
    private val bindRepo: BindRepo
) : ParentInteractor(),
    INotificationInteractor {

    override suspend fun getCount(): Int = bindRepo.getNotificationsCount()
}