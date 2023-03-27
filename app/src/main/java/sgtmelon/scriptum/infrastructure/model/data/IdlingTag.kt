package sgtmelon.scriptum.infrastructure.model.data

object IdlingTag {

    object Anim {
        private const val PREFIX = "ANIM"
        const val ICON = "${PREFIX}_ICON"
    }

    object List {
        private const val PREFIX = "LIST"
        const val CHANGE = "${PREFIX}_CHANGE"
        const val NEXT = "${PREFIX}_NEXT"
    }

    object Alarm {
        private const val PREFIX = "ALARM"
        const val START = "${PREFIX}_START"
    }
}