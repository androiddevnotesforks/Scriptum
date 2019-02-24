package sgtmelon.scriptum.office.data

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.key.NoteType

object NoteData {

    fun getTypeById(id: Int): NoteType = when (id) {
        R.id.item_add_text -> NoteType.TEXT
        R.id.item_add_roll -> NoteType.ROLL
        else -> throw NoSuchFieldException("Id doesn't match any of noteType")
    }

    object Intent {
        const val ID = "INTENT_NOTE_ID"
        const val TYPE = "INTENT_NOTE_TYPE"
    }

    object Default {
        const val ID = -1L
        const val TYPE = -1
    }

}