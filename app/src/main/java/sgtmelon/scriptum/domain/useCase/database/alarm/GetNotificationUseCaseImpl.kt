package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.dataSource.database.AlarmDataSource

class GetNotificationUseCaseImpl(
    private val dataSource: AlarmDataSource,
) : GetNotificationUseCase {

    override suspend operator fun invoke(noteId: Long): NotificationItem? {
        return dataSource.getItem(noteId)
    }
}