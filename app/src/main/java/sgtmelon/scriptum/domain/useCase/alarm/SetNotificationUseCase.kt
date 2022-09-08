package sgtmelon.scriptum.domain.useCase.alarm

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem

interface SetNotificationUseCase {

    suspend operator fun invoke(item: NotificationItem): NotificationItem?

    suspend operator fun invoke(item: NoteItem, calendar: Calendar)

    suspend operator fun invoke(item: NoteItem, date: String)
}