package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface GetNotificationListUseCase {

    suspend operator fun invoke(): List<NotificationItem>
}