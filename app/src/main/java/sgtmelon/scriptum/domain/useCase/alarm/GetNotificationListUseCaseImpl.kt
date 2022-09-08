package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.repository.database.AlarmRepo

class GetNotificationListUseCaseImpl(
    private val repository: AlarmRepo
) : GetNotificationListUseCase {

    override suspend operator fun invoke(): List<NotificationItem> = repository.getList()
}