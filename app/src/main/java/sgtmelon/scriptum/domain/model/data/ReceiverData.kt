package sgtmelon.scriptum.domain.model.data

import android.content.BroadcastReceiver
import androidx.annotation.StringDef

/**
 * Keys for work with [BroadcastReceiver]
 */
object ReceiverData {

    @StringDef(Filter.MAIN, Filter.NOTE)
    annotation class Filter {
        companion object {
            private const val PREFIX = "RECEIVER_FILTER"

            const val MAIN = "${PREFIX}_MAIN"
            const val NOTE = "${PREFIX}_NOTE"
            const val BIND = "${PREFIX}_BIND"
        }
    }

    @StringDef(Command.UNBIND_NOTE, Command.UPDATE_ALARM)
    annotation class Command {
        companion object {
            private const val PREFIX = "RECEIVER_COMMAND"

            const val UNBIND_NOTE = "${PREFIX}_UNBIND_NOTE"
            const val UPDATE_ALARM = "${PREFIX}_UPDATE_ALARM"
        }
    }

    @StringDef(Values.COMMAND, Values.NOTE_ID)
    annotation class Values {
        companion object {
            private const val PREFIX = "RECEIVER_VALUES"

            const val COMMAND = "${PREFIX}_COMMAND"
            const val NOTE_ID = "${PREFIX}_NOTE_ID"
        }
    }

}