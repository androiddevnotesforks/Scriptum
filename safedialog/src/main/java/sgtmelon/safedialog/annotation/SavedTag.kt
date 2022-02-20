package sgtmelon.safedialog.annotation

import androidx.annotation.StringDef

// TODO stringDEf
annotation class SavedTag {
    companion object {
        private const val PREFIX = "SAFE_DIALOG"

        const val POSITION = "${PREFIX}_POSITION"
        const val INIT = "${PREFIX}_INIT"
        const val VALUE = "${PREFIX}_VALUE"
        const val KEY = "${PREFIX}_KEY"
    }

    @StringDef(Blank.TITLE)
    annotation class Blank {
        companion object {
            private const val PREFIX = "SAFE_BLANK"

            const val TITLE = "${PREFIX}_TITLE"
        }
    }

    @StringDef(Options.TITLE, Options.LIST, Options.POSITION)
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

    @StringDef(Single.LIST, Single.APPLY, Single.CHECK_INIT, Single.CHECK)
    annotation class Single {
        companion object {
            private const val PREFIX = "SAFE_SINGLE"

            const val LIST = "${PREFIX}_LIST"
            const val APPLY = "${PREFIX}_APPLY"
            const val CHECK_INIT = "${PREFIX}_CHECK_INIT"
            const val CHECK = "${PREFIX}_CHECK"
        }
    }

    @StringDef(Multiple.LIST, Multiple.AT_LEAST, Multiple.CHECK_INIT, Multiple.CHECK)
    annotation class Multiple {
        companion object {
            private const val PREFIX = "SAFE_MULTIPLE"

            const val LIST = "${PREFIX}_LIST"
            const val AT_LEAST = "${PREFIX}_AT_LEAST"
            const val CHECK_INIT = "${PREFIX}_CHECK_INIT"
            const val CHECK = "${PREFIX}_CHECK"
        }
    }

    @StringDef(Time.VALUE, Time.LIST, Time.POSITION)
    annotation class Time {
        companion object {
            private const val PREFIX = "SAFE_TIME"

            const val VALUE = "${PREFIX}_VALUE"
            const val LIST = "${PREFIX}_LIST"
            const val POSITION = "${PREFIX}_POSITION"
        }
    }

    @StringDef(Date.VALUE, Date.VISIBLE, Date.POSITION)
    annotation class Date {
        companion object {
            private const val PREFIX = "SAFE_DATE"

            const val VALUE = "${PREFIX}_VALUE"
            const val VISIBLE = "${PREFIX}_VISIBLE"
            const val POSITION = "${PREFIX}_POSITION"
        }
    }
}