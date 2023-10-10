package sgtmelon.scriptum.source.ui.intent

import android.content.Context
import android.provider.Settings
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.infrastructure.utils.extensions.getPackageUri

/**
 * Interface for work with [Settings.ACTION_APPLICATION_DETAILS_SETTINGS] action in tests.
 */
interface SettingsIntent : TrackIntent {

    fun assertSettingsOpen(context: Context) {
        intended(
            allOf(
                hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS),
                hasData(getPackageUri(context))
            )
        )
    }
}