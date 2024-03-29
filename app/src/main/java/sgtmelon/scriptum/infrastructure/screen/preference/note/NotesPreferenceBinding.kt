package sgtmelon.scriptum.infrastructure.screen.preference.note

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceBinding

class NotesPreferenceBinding(fragment: PreferenceFragmentCompat) : PreferenceBinding(fragment) {

    val sortButton: Preference? get() = findPreference(R.string.pref_key_note_sort)
    val colorButton: Preference? get() = findPreference(R.string.pref_key_note_color)
    val savePeriodButton: Preference? get() = findPreference(R.string.pref_key_note_save_period)
}