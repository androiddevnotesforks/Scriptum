package sgtmelon.scriptum.idling

import androidx.annotation.StringDef

annotation class IdlingTag {

    @StringDef(Anim.TRANSITION)
    annotation class Anim {
        companion object {
            private const val PREFIX = "ANIM"

            const val TRANSITION = "${PREFIX}_TRANSITION"
        }
    }


    @StringDef(Intro.FINISH)
    annotation class Intro {
        companion object {
            private const val PREFIX = "INTRO"

            const val FINISH = "${PREFIX}_FINISH"
        }
    }

    @StringDef(Rank.LOAD_DATA)
    annotation class Rank {
        companion object {
            private const val PREFIX = "RANK"

            const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
        }
    }

    @StringDef(Notes.LOAD_DATA)
    annotation class Notes {
        companion object {
            private const val PREFIX = "NOTES"

            const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
        }
    }

    @StringDef(Bin.LOAD_DATA)
    annotation class Bin {

        companion object {
            private const val PREFIX = "BIN"

            const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
        }
    }

    @StringDef(Notification.LOAD_DATA)
    annotation class Notification {

        companion object {
            private const val PREFIX = "NOTIFICATION"

            const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
        }
    }


    @StringDef(Note.LOAD_DATA)
    annotation class Note {

        companion object {
            private const val PREFIX = "NOTE"

            const val LOAD_DATA = "${PREFIX}_LOAD_DATA"
        }
    }
}
