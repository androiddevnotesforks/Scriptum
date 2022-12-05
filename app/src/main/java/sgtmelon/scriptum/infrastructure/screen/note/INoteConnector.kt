package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface INoteConnector {

    /**
     * After save new note need update [id].
     */
    fun updateNoteId(id: Long)

    /**
     * After save note need update [color].
     */
    fun updateNoteColor(color: Color)

    fun convertNote()

    fun isOrientationChanging(): Boolean

}