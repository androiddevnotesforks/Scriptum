package sgtmelon.scriptum.domain.useCase.database.alarm

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface SetNotificationUseCase {

    suspend operator fun invoke(item: NoteItem, calendar: Calendar)

    suspend operator fun invoke(item: NoteItem, date: String)
}