package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.repository.database.AlarmRepo

class GetNotificationUseCase(private val repository: AlarmRepo) {

    suspend operator fun invoke(noteId: Long): NotificationItem? = repository.getItem(noteId)
}