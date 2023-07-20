package sgtmelon.scriptum.infrastructure.screen.preference.menu

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceBinding

class MenuPreferenceBinding(fragment: PreferenceFragmentCompat) : PreferenceBinding(fragment) {

    val themeButton: Preference? get() = findPreference(R.string.pref_key_theme)

    val backupButton: Preference? get() = findPreference(R.string.pref_key_backup)
    val noteButton: Preference? get() = findPreference(R.string.pref_key_note)
    val alarmButton: Preference? get() = findPreference(R.string.pref_key_alarm)

    val policyButton: Preference? get() = findPreference(R.string.pref_key_policy)
    val rateButton: Preference? get() = findPreference(R.string.pref_key_rate)
    val aboutButton: Preference? get() = findPreference(R.string.pref_key_about)

    val developerButton: Preference? get() = findPreference(R.string.pref_key_developer)
}