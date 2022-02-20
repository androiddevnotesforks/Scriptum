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

    @StringDef(Message.TYPE, Message.TEXT)
    annotation class Message {
        companion object {
            const val TYPE = "${PREFIX}_TYPE"
            const val TEXT = "${PREFIX}_TEXT"
        }
    }
}
