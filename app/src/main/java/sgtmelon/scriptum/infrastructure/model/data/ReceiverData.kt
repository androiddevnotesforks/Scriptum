package sgtmelon.scriptum.infrastructure.model.data

import android.content.BroadcastReceiver
import sgtmelon.scriptum.BuildConfig

/**
 * Keys for work with [BroadcastReceiver]'s.
 */
object ReceiverData {

    /** appId needed for preventing calls between different builds (e.g. ".debug") */
    object Filter {
        private const val PREFIX = "${BuildConfig.APPLICATION_ID}_RECEIVER_FILTER"

        const val RANK = "${PREFIX}_RANK"
        const val NOTES = "${PREFIX}_NOTES"
        const val BIN = "${PREFIX}_BIN"
        const val NOTE = "${PREFIX}_NOTE"
        const val NOTIFICATION = "${PREFIX}_NOTIFICATION"
        const val ALARM = "${PREFIX}_ALARM"
        const val SYSTEM = "${PREFIX}_SYSTEM"
        const val ETERNAL = "${PREFIX}_ETERNAL"
        const val DEVELOP = "${PREFIX}_DEVELOP"
    }

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

        object Eternal {
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