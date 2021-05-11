package sgtmelon.scriptum.domain.model.annotation

import androidx.annotation.StringDef
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity

/**
 * Describes keys which screen open after [SplashActivity].
 */
@StringDef(
    OpenFrom.INTENT_KEY, OpenFrom.ALARM, OpenFrom.BIND, OpenFrom.NOTIFICATIONS,
    OpenFrom.HELP_DISAPPEAR
)
annotation class OpenFrom {
    companion object {
        private const val PREFIX = "OPEN_FROM"

        const val INTENT_KEY = "${PREFIX}_INTENT_KEY"

        const val ALARM = "${PREFIX}_ALARM"
        const val BIND = "${PREFIX}_BIND"
        const val NOTIFICATIONS = "${PREFIX}_NOTIFICATIONS"
        const val HELP_DISAPPEAR = "${PREFIX}_HELP_DISAPPEAR"
    }
}