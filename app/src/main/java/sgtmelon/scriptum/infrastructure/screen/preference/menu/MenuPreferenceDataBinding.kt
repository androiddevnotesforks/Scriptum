package sgtmelon.scriptum.infrastructure.screen.preference.menu

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.findPreference

class MenuPreferenceDataBinding(val fragment: PreferenceFragmentCompat) {

    val themeButton: Preference? get() = fragment.findPreference(R.string.pref_key_theme)

    val backupButton: Preference? get() = fragment.findPreference(R.string.pref_key_backup)
    val noteButton: Preference? get() = fragment.findPreference(R.string.pref_key_note)
    val alarmButton: Preference? get() = fragment.findPreference(R.string.pref_key_alarm)

    val policyButton: Preference? get() = fragment.findPreference(R.string.pref_key_policy)
    val rateButton: Preference? get() = fragment.findPreference(R.string.pref_key_rate)
    val helpButton: Preference? get() = fragment.findPreference(R.string.pref_key_help)
    val aboutButton: Preference? get() = fragment.findPreference(R.string.pref_key_about)

    val developerButton: Preference? get() = fragment.findPreference(R.string.pref_key_developer)
}