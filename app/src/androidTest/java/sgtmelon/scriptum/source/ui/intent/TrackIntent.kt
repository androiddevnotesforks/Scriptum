package sgtmelon.scriptum.source.ui.intent

import androidx.test.espresso.intent.Intents

/**
 * Interface with simple realization for tracking intents.
 */
interface TrackIntent {

    fun trackIntent(func: () -> Unit) {
        Intents.init()
        func()
        Intents.release()
    }
}