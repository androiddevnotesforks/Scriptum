package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.toUri
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment

/**
 * Fragment of help preferences.
 */
class HelpPrefFragment : ParentPreferenceFragment() {


    private val policyPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_help_privacy_policy)) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_help, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onSetup()
    }

    private fun onSetup() {
        policyPreference?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = BuildConfig.PRIVACY_POLICY_URL.toUri()
            })

            return@setOnPreferenceClickListener true
        }
    }
}