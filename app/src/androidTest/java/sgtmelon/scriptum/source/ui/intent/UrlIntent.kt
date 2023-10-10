package sgtmelon.scriptum.source.ui.intent

import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.infrastructure.converter.UriConverter

/**
 * Interface for work with [Intent.ACTION_VIEW] action in tests.
 */
interface UrlIntent : TrackIntent {

    fun assertUrlOpen(url: String) {
        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(UriConverter().toUri(url))
            )
        )
    }
}