package sgtmelon.scriptum.domain.useCase.note.cacheNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

interface CacheNoteUseCase<T : NoteItem> {

    /** Return null if nothing was cached. */
    val item: T?

    operator fun invoke(item: T)

}