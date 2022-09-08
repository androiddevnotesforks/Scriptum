package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

class DeleteNotificationUseCaseImpl(
    private val repository: AlarmRepo
) : DeleteNotificationUseCase {

    override suspend fun invoke(item: NoteItem) = invoke(item.id)

    override suspend fun invoke(item: NotificationItem) = invoke(item.note.id)

    override suspend operator fun invoke(noteId: Long) = repository.delete(noteId)
}