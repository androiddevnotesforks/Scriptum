package sgtmelon.scriptum.source.ui.intent

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import org.hamcrest.Matchers.allOf
import sgtmelon.scriptum.infrastructure.utils.extensions.mailtoUri

/**
 * Interface for work with [Intent.ACTION_SENDTO] action in tests.
 */
interface SendEmailIntent : TrackIntent {

    fun assertSendEmail(
        context: Context,
        @StringRes email: Int,
        @StringRes subject: Int
    ) {
        intended(
            allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasData(mailtoUri),
                hasExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(email))),
                hasExtra(Intent.EXTRA_SUBJECT, context.getString(subject))
            )
        )
    }
}