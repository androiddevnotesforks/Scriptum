package sgtmelon.scriptum.domain.useCase.note.cacheNote

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.utils.extensions.note.deepCopy

class CacheRollNoteUseCase : CacheNoteUseCase<NoteItem.Roll> {

    override var item: NoteItem.Roll? = null

    override operator fun invoke(item: NoteItem.Roll) {
        this.item = item.deepCopy()
    }
}