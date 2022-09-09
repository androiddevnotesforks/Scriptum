package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.data.repository.database.AlarmRepo

class DeleteNotificationUseCase(private val repository: AlarmRepo) {

    suspend operator fun invoke(item: NoteItem) = invoke(item.id)

    suspend operator fun invoke(item: NotificationItem) = invoke(item.note.id)

    suspend operator fun invoke(noteId: Long) = repository.delete(noteId)
}