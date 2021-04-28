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
            const val ETERNAL = "${PREFIX}_BIND"
        }
    }

    @StringDef(Command.UNBIND_NOTE, Command.UPDATE_ALARM)
    annotation class Command {
        companion object {
            private const val PREFIX = "RECEIVER_COMMAND"

            const val UNBIND_NOTE = "${PREFIX}_UNBIND_NOTE"
            const val UPDATE_ALARM = "${PREFIX}_UPDATE_ALARM"
        }

        @StringDef(
            Eternal.SET_ALARM, Eternal.CANCEL_ALARM,
            Eternal.NOTIFY_NOTES, Eternal.CANCEL_NOTE, Eternal.NOTIFY_INFO
        )
        annotation class Eternal {
            companion object {
                const val SET_ALARM = "${PREFIX}_SET_ALARM"
                const val CANCEL_ALARM = "${PREFIX}_CANCEL_ALARM"
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