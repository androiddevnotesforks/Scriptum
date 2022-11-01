package sgtmelon.scriptum.infrastructure.model.annotation

import androidx.annotation.StringDef
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity

/**
 * Describes keys for start different screens after launch of [SplashActivity].
 *
 * !!! BE CAREFUL !!! because some keys from here is used inside xml/shortcuts.xml.
 */
@StringDef(
    AppOpenFrom.INTENT_KEY,
    AppOpenFrom.ALARM, AppOpenFrom.BIND_NOTE,
    AppOpenFrom.NOTIFICATIONS, AppOpenFrom.HELP_DISAPPEAR,
    AppOpenFrom.CREATE_TEXT, AppOpenFrom.CREATE_ROLL
)
annotation class AppOpenFrom {
    companion object {
        const val INTENT_KEY = "OPEN_FROM_INTENT_KEY"

        const val ALARM = "OPEN_FROM_ALARM"
        const val BIND_NOTE = "OPEN_FROM_BIND"
        const val NOTIFICATIONS = "OPEN_FROM_NOTIFICATIONS"
        const val HELP_DISAPPEAR = "OPEN_FROM_HELP_DISAPPEAR"
        const val CREATE_TEXT = "OPEN_FROM_CREATE_TEXT"
        const val CREATE_ROLL = "OPEN_FROM_CREATE_ROLL"
    }
}