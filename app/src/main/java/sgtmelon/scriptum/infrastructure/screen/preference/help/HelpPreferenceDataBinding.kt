package sgtmelon.scriptum.infrastructure.screen.preference.help

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.findPreference

class HelpPreferenceDataBinding(val fragment: PreferenceFragmentCompat) {

    val disappearButton: Preference? get() = fragment.findPreference(R.string.pref_key_help_disappear)

}