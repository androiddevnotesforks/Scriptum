package sgtmelon.scriptum.model.key

import android.content.BroadcastReceiver

/**
 * Ключи для работы с [BroadcastReceiver]
 *
 * @author SerjantArbuz
 */
object ReceiverKey {

    // TODO перевести в data

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

        const val ID_UNDEFINED = -1L

        const val COMMAND = "${PREFIX}_COMMAND"
        const val NOTE_ID = "${PREFIX}_NOTE_ID"
    }

}