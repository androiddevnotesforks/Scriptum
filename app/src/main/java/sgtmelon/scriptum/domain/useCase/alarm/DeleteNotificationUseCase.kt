package sgtmelon.scriptum.domain.useCase.alarm

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface DeleteNotificationUseCase {

    suspend operator fun invoke(item: NoteItem)

    suspend operator fun invoke(item: NotificationItem)

    suspend operator fun invoke(noteId: Long)
}