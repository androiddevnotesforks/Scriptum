package sgtmelon.scriptum.source.ui.intent

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasFlag
import org.hamcrest.Matchers.allOf


/**
 * Interface for work with [Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS] action in tests.
 */
interface SettingsChannelIntent : TrackIntent {

    fun assertSettingsChannelOpen(context: Context, @StringRes id: Int) {
        intended(
            allOf(
                hasAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS),
                hasFlag(Intent.FLAG_ACTIVITY_NEW_TASK),
                hasExtra(Settings.EXTRA_APP_PACKAGE, context.packageName),
                hasExtra(Settings.EXTRA_CHANNEL_ID, context.getString(id))
            )
        )
    }
}