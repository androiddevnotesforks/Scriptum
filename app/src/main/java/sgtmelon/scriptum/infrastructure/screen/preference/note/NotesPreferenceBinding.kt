package sgtmelon.scriptum.infrastructure.screen.preference.note

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.findPreference

class NotesPreferenceBinding(val fragment: PreferenceFragmentCompat) {

    val sortButton: Preference? get() = fragment.findPreference(R.string.pref_key_note_sort)
    val colorButton: Preference? get() = fragment.findPreference(R.string.pref_key_note_color)
    val savePeriodButton: Preference? get() = fragment.findPreference(R.string.pref_key_note_save_period)
}