package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

class GetNotificationUseCaseImpl(
    private val repository: AlarmRepo
) : GetNotificationUseCase {

    override suspend operator fun invoke(noteId: Long): NotificationItem? {
        return repository.getItem(noteId)
    }
}