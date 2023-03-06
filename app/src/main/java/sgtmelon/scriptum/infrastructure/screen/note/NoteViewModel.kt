package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

interface NoteViewModel {

    val defaultColor: Color

    fun convertType(type: NoteType): NoteType

}