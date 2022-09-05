package sgtmelon.scriptum.domain.useCase.database.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface GetNotificationUseCase {

    suspend operator fun invoke(noteId: Long): NotificationItem?
}