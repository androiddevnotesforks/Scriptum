package sgtmelon.scriptum.infrastructure.model.data

object IdlingTag {

    object Anim {
        private const val PREFIX = "ANIM"
        const val ICON = "${PREFIX}_ICON"
    }

    object List {
        private const val PREFIX = "LIST"

        val RANK = "${PREFIX}_RANK_CHANGE" to "${PREFIX}_RANK_NEXT"
        val NOTE = "${PREFIX}_NOTE_CHANGE" to "${PREFIX}_NOTE_NEXT"
        val ROLL = "${PREFIX}_ROLL_CHANGE" to "${PREFIX}_ROLL_NEXT"
        val NOTIFICATION = "${PREFIX}_NOTIFICATION_CHANGE" to "${PREFIX}_NOTIFICATION_NEXT"
        val PRINT = "${PREFIX}_PRINT_CHANGE" to "${PREFIX}_PRINT_NEXT"

    }

    object Alarm {
        private const val PREFIX = "ALARM"
        const val START = "${PREFIX}_START"
    }
}