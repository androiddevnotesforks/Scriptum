package sgtmelon.scriptum.infrastructure.screen.preference.note

import androidx.lifecycle.Lifecycle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceBinding
import sgtmelon.scriptum.infrastructure.utils.findPreference

class NotePreferenceDataBinding(
    lifecycle: Lifecycle,
    fragment: PreferenceFragmentCompat
) : ParentPreferenceBinding(lifecycle, fragment) {

    val sortButton: Preference? get() = fragment?.findPreference(R.string.pref_key_note_sort)
    val colorButton: Preference? get() = fragment?.findPreference(R.string.pref_key_note_color)
    val savePeriodButton: Preference? get() = fragment?.findPreference(R.string.pref_key_note_time)
}