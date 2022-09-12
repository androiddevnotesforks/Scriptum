package sgtmelon.scriptum.cleanup.domain.model.data

import android.content.BroadcastReceiver
import androidx.annotation.StringDef
import sgtmelon.scriptum.BuildConfig

/**
 * Keys for work with [BroadcastReceiver]
 */
object ReceiverData {

    @StringDef(Filter.MAIN, Filter.NOTE, Filter.SYSTEM, Filter.ETERNAL, Filter.DEVELOP)
    annotation class Filter {
        companion object {
            /** appId needed for preventing calls between different builds (e.g. ".debug") */
            private const val PREFIX = "${BuildConfig.APPLICATION_ID}_RECEIVER_FILTER"

            const val MAIN = "${PREFIX}_MAIN"
            const val NOTE = "${PREFIX}_NOTE"
            const val SYSTEM = "${PREFIX}_SYSTEM"
            const val ETERNAL = "${PREFIX}_ETERNAL"
            const val DEVELOP = "${PREFIX}_DEVELOP"
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
            System.NOTIFY_NOTES, System.CANCEL_NOTE, System.NOTIFY_INFO,
            System.CLEAR_BIND, System.CLEAR_ALARM
        )
        annotation class System {
            companion object {
                const val TIDY_UP_ALARM = "${PREFIX}_TIDY_UP_ALARM"
                const val SET_ALARM = "${PREFIX}_SET_ALARM"
                const val CANCEL_ALARM = "${PREFIX}_CANCEL_ALARM"
                const val NOTIFY_NOTES = "${PREFIX}_NOTIFY_ALL"
                const val CANCEL_NOTE = "${PREFIX}_CANCEL_NOTE"
                const val NOTIFY_INFO = "${PREFIX}_NOTIFY_INFO"
                const val CLEAR_BIND = "${PREFIX}_CLEAR_BIND"
                const val CLEAR_ALARM = "${PREFIX}_CLEAR_ALARM"
            }
        }

        @StringDef(Eternal.KILL, Eternal.PING, Eternal.PONG)
        annotation class Eternal {
            companion object {
                const val KILL = "${PREFIX}_KILL"
                const val PING = "${PREFIX}_PING"
                const val PONG = "${PREFIX}_PONG"
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