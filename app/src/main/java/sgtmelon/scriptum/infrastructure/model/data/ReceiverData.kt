package sgtmelon.scriptum.infrastructure.model.data

import android.content.BroadcastReceiver

/**
 * Keys for work with [BroadcastReceiver]'s.
 */
object ReceiverData {

    object Command {
        private const val PREFIX = "RECEIVER_COMMAND"

        object UI {
            const val UNBIND_NOTE = "${PREFIX}_UNBIND_NOTE"
            const val INFO_CHANGE = "${PREFIX}_INFO_CHANGE"
        }

        object System {
            const val TIDY_UP_ALARM = "${PREFIX}_TIDY_UP_ALARM"
            const val SET_ALARM = "${PREFIX}_SET_ALARM"
            const val CANCEL_ALARM = "${PREFIX}_CANCEL_ALARM"
            const val NOTIFY_NOTES = "${PREFIX}_NOTIFY_ALL"
            const val CANCEL_NOTE = "${PREFIX}_CANCEL_NOTE"
            const val NOTIFY_INFO = "${PREFIX}_NOTIFY_INFO"
            const val CLEAR_BIND = "${PREFIX}_CLEAR_BIND"
            const val CLEAR_ALARM = "${PREFIX}_CLEAR_ALARM"
        }

        object Develop {
            const val KILL = "${PREFIX}_KILL"
            const val PING = "${PREFIX}_PING"
            const val PONG = "${PREFIX}_PONG"
        }
    }

    object Values {
        private const val PREFIX = "RECEIVER_VALUES"
        const val COMMAND = "${PREFIX}_COMMAND"
    }
}