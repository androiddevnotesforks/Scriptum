package sgtmelon.scriptum.model.data

import android.content.BroadcastReceiver

/**
 * Ключи для работы с [BroadcastReceiver]
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
    }

    object Values {
        private const val PREFIX = "RECEIVER_VALUES"

        const val ND_NOTE_ID = -1L
        const val ND_NOTE_COLOR = -1

        const val COMMAND = "${PREFIX}_COMMAND"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
        const val NOTE_COLOR = "${PREFIX}_COLOR"
    }

}