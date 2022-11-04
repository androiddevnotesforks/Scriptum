package sgtmelon.scriptum.infrastructure.model.data

object IdlingTag {

    object Anim {
        private const val PREFIX = "ANIM"
        const val ICON = "${PREFIX}_ICON"
    }

    object Alarm {
        private const val PREFIX = "ALARM"

        const val CONFIGURE = "${PREFIX}_CONFIGURE"
        const val START = "${PREFIX}_START"
    }

    object Rank {
        private const val PREFIX = "RANK"
        const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
    }

    object Notes {
        private const val PREFIX = "NOTES"
        const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
    }

    object Bin {
        private const val PREFIX = "BIN"
        const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
    }

    object Notification {
        private const val PREFIX = "NOTIFICATION"
        const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
    }


    object Note {
        private const val PREFIX = "NOTE"
        const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
    }
}