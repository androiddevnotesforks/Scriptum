package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.StringDef
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity

/**
 * Describes keys which screen open after [SplashActivity].
 *
 * !!! Also be careful, because some keys from here hides inside xml/shortcuts.xml
 */
@StringDef(
    OpenFrom.INTENT_KEY,
    OpenFrom.ALARM, OpenFrom.BIND, OpenFrom.NOTIFICATIONS, OpenFrom.HELP_DISAPPEAR,
    OpenFrom.CREATE_TEXT, OpenFrom.CREATE_ROLL
)
annotation class OpenFrom {
    companion object {
        const val INTENT_KEY = "OPEN_FROM_INTENT_KEY"

        const val ALARM = "OPEN_FROM_ALARM"
        const val BIND = "OPEN_FROM_BIND"
        const val NOTIFICATIONS = "OPEN_FROM_NOTIFICATIONS"
        const val HELP_DISAPPEAR = "OPEN_FROM_HELP_DISAPPEAR"
        const val CREATE_TEXT = "OPEN_FROM_CREATE_TEXT"
        const val CREATE_ROLL = "OPEN_FROM_CREATE_ROLL"
    }
}