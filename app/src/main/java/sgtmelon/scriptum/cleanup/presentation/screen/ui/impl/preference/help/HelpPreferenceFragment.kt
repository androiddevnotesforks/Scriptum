package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help

import android.os.Bundle
import androidx.preference.Preference
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getSiteIntent
import sgtmelon.scriptum.cleanup.extension.startActivitySafe
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory

/**
 * Fragment of help preferences.
 */
class HelpPreferenceFragment : ParentPreferenceFragment() {

    private val notificationPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_help_notification_disappear)) }
    private val policyPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_help_privacy_policy)) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_help, rootKey)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onSetup()
    }

    private fun onSetup() {
        notificationPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(InstanceFactory.Preference.HelpDisappear[context])
            }

            return@setOnPreferenceClickListener true
        }
        policyPreference?.setOnPreferenceClickListener {
            onPolicyClick()
            return@setOnPreferenceClickListener true
        }
    }

    private fun onPolicyClick() {
        val context = context ?: return

        val intent = getSiteIntent(BuildConfig.PRIVACY_POLICY_URL)
        context.startActivitySafe(intent, delegators.toast)
    }
}