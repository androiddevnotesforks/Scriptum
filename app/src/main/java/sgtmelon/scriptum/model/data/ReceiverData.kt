package sgtmelon.scriptum.model.data

import android.content.BroadcastReceiver

/**
 * Keys for work with [BroadcastReceiver]
 */
object ReceiverData {

    object Filter {
        private const val PREFIX = "RECEIVER_FILTER"

        const val MAIN = "${PREFIX}_MAIN"
        const val NOTE = "${PREFIX}_NOTE"
    }

    object Command {
        private const val PREFIX = "RECEIVER_COMMAND"

        const val UNBIND_NOTE = "${PREFIX}_UNBIND_NOTE"
        const val UPDATE_ALARM = "${PREFIX}_UPDATE_ALARM"
    }

    object Values {
        private const val PREFIX = "RECEIVER_VALUES"

        const val COMMAND = "${PREFIX}_COMMAND"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
    }

}