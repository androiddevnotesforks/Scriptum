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

        @StringDef(Bind.NOTIFY_NOTES, Bind.CANCEL_NOTE, Bind.NOTIFY_INFO)
        annotation class Bind {
            companion object {
                const val NOTIFY_NOTES = "${PREFIX}_NOTIFY_ALL"
                const val CANCEL_NOTE = "${PREFIX}_CANCEL_NOTE"
                const val NOTIFY_INFO = "${PREFIX}_NOTIFY_INFO"
            }
        }
    }

    @StringDef(Values.COMMAND)
    annotation class Values {
        companion object {
            private const val PREFIX = "RECEIVER_VALUES"

            const val COMMAND = "${PREFIX}_COMMAND"
        }
    }

}