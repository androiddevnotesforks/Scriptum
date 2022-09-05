package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

class GetNotificationListUseCaseImpl(
    private val dataSource: AlarmDataSource,
) : GetNotificationListUseCase {

    override suspend operator fun invoke(): List<NotificationItem> = dataSource.getItemList()
}