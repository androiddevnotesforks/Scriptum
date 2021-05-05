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
            const val SYSTEM = "${PREFIX}_SYSTEM"
        }
    }

    annotation class Command {
        companion object {
            private const val PREFIX = "RECEIVER_COMMAND"
        }

        @StringDef(UI.UNBIND_NOTE, UI.UPDATE_ALARM)
        annotation class UI {
            companion object {
                const val UNBIND_NOTE = "${PREFIX}_UNBIND_NOTE"
                const val UPDATE_ALARM = "${PREFIX}_UPDATE_ALARM"
            }
        }

        @StringDef(
            System.TIDY_UP_ALARM, System.SET_ALARM, System.CANCEL_ALARM,
            System.NOTIFY_NOTES, System.CANCEL_NOTE, System.NOTIFY_INFO
        )
        annotation class System {
            companion object {
                const val TIDY_UP_ALARM = "${PREFIX}_TIDY_UP_ALARM"
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