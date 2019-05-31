package sgtmelon.scriptum.model.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.key.NoteType

object NoteData {

    fun getTypeById(id: Int): NoteType = when (id) {
        R.id.item_add_text -> NoteType.TEXT
        R.id.item_add_roll -> NoteType.ROLL
        else -> throw NoSuchFieldException("Id doesn't match any of noteType")
    }

    object Intent {
        private const val PREFIX = "INTENT_NOTE"

        const val ID = "${PREFIX}_ID"
        const val TYPE = "${PREFIX}_TYPE"
        const val COLOR = "${PREFIX}_COLOR"
    }

    object Default {
        const val ID = -1L
        const val TYPE = -1
    }

}