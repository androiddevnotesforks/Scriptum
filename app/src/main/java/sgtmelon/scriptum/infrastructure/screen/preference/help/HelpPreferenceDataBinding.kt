package sgtmelon.scriptum.infrastructure.screen.preference.help

import androidx.lifecycle.Lifecycle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceBinding
import sgtmelon.scriptum.infrastructure.utils.findPreference

class HelpPreferenceDataBinding(
    lifecycle: Lifecycle,
    fragment: PreferenceFragmentCompat?
) : ParentPreferenceBinding(lifecycle, fragment) {

    val disappearButton: Preference? get() = fragment?.findPreference(R.string.pref_key_help_disappear)
    val policyButton: Preference? get() = fragment?.findPreference(R.string.pref_key_help_policy)

}