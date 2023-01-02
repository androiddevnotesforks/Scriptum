package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.utils.extensions.note.joinToText

class GetCopyTextUseCase(private val repository: NoteRepo) {

    suspend operator fun invoke(item: NoteItem): String {
        val builder = StringBuilder()

        if (item.name.isNotEmpty()) {
            builder.append(item.name).append("\n")
        }

        when (item) {
            is NoteItem.Text -> builder.append(item.text)
            is NoteItem.Roll -> builder.append(repository.getRollList(item.id).joinToText())
        }

        return builder.toString()
    }
}