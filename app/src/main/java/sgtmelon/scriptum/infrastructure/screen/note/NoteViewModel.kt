package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

interface NoteViewModel {

    fun convertType(type: NoteType): NoteType

}