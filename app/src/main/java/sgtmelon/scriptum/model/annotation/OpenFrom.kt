package sgtmelon.scriptum.model.annotation

import androidx.annotation.StringDef
import sgtmelon.scriptum.screen.ui.SplashActivity

/**
 * Describes keys which screen open after [SplashActivity]
 */
@StringDef(OpenFrom.BIND, OpenFrom.ALARM)
annotation class OpenFrom {

    companion object {
        private const val PREFIX = "OPEN_FROM"

        const val INTENT_KEY = "${PREFIX}_INTENT_KEY"

        const val ALARM = "${PREFIX}_ALARM"
        const val BIND = "${PREFIX}_BIND"
        const val INFO = "${PREFIX}_INFO"
    }

}