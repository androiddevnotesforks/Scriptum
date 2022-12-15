package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface INoteConnector {

    /**
     * After save new note need update [id].
     */
    @Deprecated("from child fragment update value directly inside bundle provider")
    fun updateNoteId(id: Long)

    /**
     * After save note need update [color].
     */
    @Deprecated("from child fragment update value directly inside bundle provider")
    fun updateNoteColor(color: Color)

    fun convertNote()

    fun isOrientationChanging(): Boolean

}