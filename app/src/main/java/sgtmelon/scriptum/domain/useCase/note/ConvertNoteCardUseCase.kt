package sgtmelon.scriptum.domain.useCase.note

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.test.prod.RunPrivate

class ConvertNoteCardUseCase(private val noteRepo: NoteRepo) {

    suspend operator fun invoke(item: NoteItem): NoteItem {
        val newItem = when (item) {
            is NoteItem.Text -> noteRepo.convertNote(item)
            is NoteItem.Roll -> noteRepo.convertNote(item, useCache = false)
        }

        if (newItem is NoteItem.Roll) {
            onConvertOptimisation(newItem)
        }

        return newItem
    }

    /**
     * Optimisation for get only first 4 items in roll list.
     */
    @RunPrivate fun onConvertOptimisation(item: NoteItem.Roll) {
        val previewSize = NoteItem.Roll.PREVIEW_SIZE
        val list = item.list

        while (list.size > previewSize) {
            list.removeLast()
        }
    }
}