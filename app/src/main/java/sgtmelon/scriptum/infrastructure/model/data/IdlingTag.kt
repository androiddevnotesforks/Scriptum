package sgtmelon.scriptum.infrastructure.model.data

object IdlingTag {

    object Anim {
        private const val PREFIX = "ANIM"
        const val ICON = "${PREFIX}_ICON"
    }

    object Alarm {
        private const val PREFIX = "ALARM"

        const val CONFIGURE = "${PREFIX}_CONFIGURE"
        const val ANIM = "${PREFIX}_ANIM"
        const val START = "${PREFIX}_START"
    }
}