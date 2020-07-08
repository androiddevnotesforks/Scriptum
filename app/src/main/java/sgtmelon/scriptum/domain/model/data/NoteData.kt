package sgtmelon.scriptum.domain.model.data

import sgtmelon.scriptum.domain.model.annotation.Color

object NoteData {

    object Intent {
        private const val PREFIX = "INTENT_NOTE"

        const val ID = "${PREFIX}_ID"
        const val COLOR = "${PREFIX}_COLOR"
        const val TYPE = "${PREFIX}_TYPE"
    }

    object Default {
        const val ID = -1L
        const val COLOR = Color.UNDEFINED
        const val TYPE = -1
    }

}