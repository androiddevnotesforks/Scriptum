package sgtmelon.safedialog.annotation

import androidx.annotation.StringDef

@StringDef(
    SavedTag.TITLE,
    SavedTag.MESSAGE,
    SavedTag.POSITION,
    SavedTag.INIT,
    SavedTag.VALUE,
    SavedTag.VISIBLE,
    SavedTag.TIME,
    SavedTag.LIST,
    SavedTag.KEY
)
// TODO stringDEf
annotation class SavedTag {
    companion object {
        private const val PREFIX = "SAFE_DIALOG"

        const val TITLE = "${PREFIX}_TITLE"
        const val MESSAGE = "${PREFIX}_MESSAGE"
        const val POSITION = "${PREFIX}_POSITION"
        const val INIT = "${PREFIX}_INIT"
        const val VALUE = "${PREFIX}_VALUE"
        const val VISIBLE = "${PREFIX}_VISIBLE"
        const val TIME = "${PREFIX}_TIME"
        const val LIST = "${PREFIX}_LIST"
        const val KEY = "${PREFIX}_KEY"
    }

    annotation class Blank {
        companion object {
            private const val PREFIX = "SAFE_BLANK"

            const val TITLE = "${PREFIX}_TITLE"
        }
    }

    annotation class Options {
        companion object {
            private const val PREFIX = "SAFE_OPTIONS"

            const val TITLE = "${PREFIX}_TITLE"
            const val LIST = "${PREFIX}_LIST"
            const val POSITION = "${PREFIX}_POSITION"
        }
    }

    @StringDef(Message.TYPE, Message.TEXT)
    annotation class Message {
        companion object {
            private const val PREFIX = "SAFE_MESSAGE"

            const val TYPE = "${PREFIX}_TYPE"
            const val TEXT = "${PREFIX}_TEXT"
        }
    }

    annotation class Time {
        companion object {
            private const val PREFIX = "SAFE_TIME"

            const val VALUE = "${PREFIX}_VALUE"
            const val LIST = "${PREFIX}_LIST"
            const val POSITION = "${PREFIX}_POSITION"
        }
    }
}
