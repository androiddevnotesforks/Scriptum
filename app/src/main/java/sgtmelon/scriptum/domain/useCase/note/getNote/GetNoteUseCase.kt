package sgtmelon.scriptum.domain.useCase.note.getNote

import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

abstract class GetNoteUseCase<T : NoteItem>(private val repository: NoteRepo) {

    abstract fun compare(item: NoteItem): Boolean

    suspend operator fun invoke(noteId: Long): T? {
        TODO()
        //        val noteItem = repository.getItem(noteId, isOptimal = false)
        //
        //        if (noteItem == null || noteItem !is T) return null
        //
        //        return noteItem
    }
}