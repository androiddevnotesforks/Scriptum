package sgtmelon.scriptum.infrastructure.model.data

object IdlingTag {

    object Anim {
        private const val PREFIX = "ANIM"
        const val ICON = "${PREFIX}_ICON"
        const val LOAD = "${PREFIX}_LOAD"
    }

    object Alarm {
        private const val PREFIX = "ALARM"
        const val START = "${PREFIX}_START"
    }
}