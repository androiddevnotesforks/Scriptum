package sgtmelon.scriptum.presentation.screen.ui.impl.preference.help

import android.os.Bundle
import androidx.preference.Preference
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment

/**
 * Fragment of help preferences.
 */
class HelpPreferenceFragment : ParentPreferenceFragment() {

    private val notificationPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_help_notification_disappear)) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_help, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onSetup()
    }

    private fun onSetup() {
        notificationPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(HelpDisappearActivity[context])
            }

            return@setOnPreferenceClickListener true
        }
    }
}