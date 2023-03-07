package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.init.NoteInit
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface NoteConnector {

    val init: NoteInit

    fun updateHolder(color: Color)

    fun convertNote(item: NoteItem)


    @Deprecated("Improve it, I don't think it's work correct with split screen for example.")
    fun isOrientationChanging(): Boolean

}