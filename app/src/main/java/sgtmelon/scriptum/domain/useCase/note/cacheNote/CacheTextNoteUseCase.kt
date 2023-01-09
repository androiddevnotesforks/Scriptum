package sgtmelon.scriptum.domain.useCase.note.cacheNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.utils.extensions.note.copy

class CacheTextNoteUseCase : CacheNoteUseCase<NoteItem.Text> {

    override var item: NoteItem.Text? = null

    override operator fun invoke(item: NoteItem.Text) {
        this.item = item.copy()
    }
}