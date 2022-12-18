package sgtmelon.scriptum.infrastructure.model.exception.note

import kotlin.reflect.KClass
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

class IllegalNoteTypeException(expected: KClass<*>, noteItem: NoteItem?) : IllegalStateException(
    "Get wrong note type | " +
            "expected=${expected.simpleName}, " +
            "actual=${noteItem?.javaClass?.simpleName}"
)