package sgtmelon.safedialog.annotation

import androidx.annotation.StringDef

/**
 * Annotation for save instance tags. Use it for work with Bundle.
 *
 * If dialog outside of this module - use [Common].
 */
annotation class SavedTag {

    @StringDef(
        Common.TITLE, Common.POSITION,
        Common.VALUE_INIT, Common.VALUE,
        Common.LIST, Common.KEY
    )
    annotation class Common {
        companion object {
            const val TITLE = "SAFE_COMMON_TITLE"
            const val POSITION = "SAFE_COMMON_POSITION"
            const val VALUE_INIT = "SAFE_COMMON_VALUE_INIT"
            const val VALUE = "SAFE_COMMON_VALUE"
            const val LIST = "SAFE_COMMON_LIST"
            const val KEY = "SAFE_COMMON_KEY"
        }
    }

    @StringDef(Blank.TITLE)
    annotation class Blank {
        companion object {
            const val TITLE = "SAFE_BLANK_TITLE"
        }
    }

    @StringDef(Options.TITLE, Options.LIST, Options.POSITION)
    annotation class Options {
        companion object {
            const val TITLE = "SAFE_OPTIONS_TITLE"
            const val LIST = "SAFE_OPTIONS_LIST"
            const val POSITION = "SAFE_OPTIONS_POSITION"
        }
    }

    @StringDef(Message.TYPE, Message.TEXT)
    annotation class Message {
        companion object {
            const val TYPE = "SAFE_MESSAGE_TYPE"
            const val TEXT = "SAFE_MESSAGE_TEXT"
        }
    }

    @StringDef(Single.LIST, Single.APPLY, Single.CHECK_INIT, Single.CHECK)
    annotation class Single {
        companion object {
            const val LIST = "SAFE_SINGLE_LIST"
            const val APPLY = "SAFE_SINGLE_APPLY"
            const val CHECK_INIT = "SAFE_SINGLE_CHECK_INIT"
            const val CHECK = "SAFE_SINGLE_CHECK"
        }
    }

    @StringDef(Multiple.LIST, Multiple.AT_LEAST, Multiple.CHECK_INIT, Multiple.CHECK)
    annotation class Multiple {
        companion object {
            const val LIST = "SAFE_MULTIPLE_LIST"
            const val AT_LEAST = "SAFE_MULTIPLE_AT_LEAST"
            const val CHECK_INIT = "SAFE_MULTIPLE_CHECK_INIT"
            const val CHECK = "SAFE_MULTIPLE_CHECK"
        }
    }

    @StringDef(Time.VALUE, Time.LIST, Time.POSITION)
    annotation class Time {
        companion object {
            const val VALUE = "SAFE_TIME_VALUE"
            const val LIST = "SAFE_TIME_LIST"
            const val POSITION = "SAFE_TIME_POSITION"
        }
    }

    @StringDef(Date.VALUE, Date.VISIBLE, Date.POSITION)
    annotation class Date {
        companion object {
            const val VALUE = "SAFE_DATE_VALUE"
            const val VISIBLE = "SAFE_DATE_VISIBLE"
            const val POSITION = "SAFE_DATE_POSITION"
        }
    }
}