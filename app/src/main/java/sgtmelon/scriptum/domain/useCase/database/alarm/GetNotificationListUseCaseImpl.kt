package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

class GetNotificationListUseCaseImpl(
    private val repository: AlarmRepo
) : GetNotificationListUseCase {

    override suspend operator fun invoke(): List<NotificationItem> = repository.getList()
}