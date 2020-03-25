package sgtmelon.scriptum.domain.model.data

object NoteData {

    object Intent {
        private const val PREFIX = "INTENT_NOTE"

        const val ID = "${PREFIX}_ID"
        const val COLOR = "${PREFIX}_COLOR"
        const val TYPE = "${PREFIX}_TYPE"
    }

    object Default {
        const val ID = -1L
        const val COLOR = -1
        const val TYPE = -1
    }

}