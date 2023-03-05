package sgtmelon.scriptum.infrastructure.screen

import android.content.Context
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity

/**
 * Class providing access to screen intents.
 */
object Screens {

    fun toMain(context: Context) = context.intent<MainActivity>()

}